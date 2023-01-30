package com.hejwesele.datastore

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hejwesele.datastore.DatastoreResult.Failure
import com.hejwesele.datastore.DatastoreResult.Success
import com.hejwesele.datastore.dto.event.EventDto
import com.hejwesele.datastore.dto.home.HomeTileDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Suppress("TooGenericExceptionCaught", "SwallowedException")
@OptIn(ExperimentalCoroutinesApi::class)
internal class Firestore @Inject constructor() : Datastore {

    private val firestoreReference = Firebase.firestore
    private val firestoreScope = CoroutineScope(Dispatchers.Default)

    override suspend fun getEvent(eventId: String): DatastoreResult<EventDto> = suspendCoroutine { continuation ->

        firestoreReference
            .collection(Collections.events)
            .document(eventId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    try {
                        val event = requireNotNull(snapshot.toObject(EventDto::class.java))
                        continuation.resume(Success(event))
                    } catch (exception: RuntimeException) {
                        continuation.resume(Failure(exception))
                    }
                } else {
                    continuation.resume(Failure(DocumentNotExistsException(snapshot.id)))
                }
            }
            .addOnFailureListener { exception ->
                continuation.resume(Failure(exception))
            }
    }

    override suspend fun getAllEvents(): DatastoreResult<List<EventDto>> = suspendCoroutine { continuation ->
        firestoreReference
            .collection(Collections.events)
            .get()
            .addOnSuccessListener { snapshot ->
                continuation.resume(
                    Success(
                        snapshot.documents
                            .filter { it.exists() }
                            .mapNotNull {
                                try {
                                    requireNotNull(it.toObject(EventDto::class.java))
                                } catch (exception: RuntimeException) {
                                    null
                                }
                            }
                    )
                )
            }
            .addOnFailureListener { exception ->
                continuation.resume(Failure(exception))
            }
    }

    override suspend fun getHomeTiles(eventId: String): DatastoreResult<List<HomeTileDto>> = suspendCoroutine { continuation ->
        firestoreReference
            .collection(Collections.events)
            .document(eventId)
            .collection(Collections.homeTiles)
            .get()
            .addOnSuccessListener { snapshot ->
                continuation.resume(
                    Success(
                        snapshot.documents
                            .filter { it.exists() }
                            .mapNotNull {
                                try {
                                    requireNotNull(it.toObject(HomeTileDto::class.java))
                                } catch (e: RuntimeException) {
                                    null
                                }
                            }
                    )
                )
            }
            .addOnFailureListener { exception ->
                continuation.resume(Failure(exception))
            }
    }

    override suspend fun observeEvent(eventId: String): SharedFlow<EventDto> {
        val snapshots = MutableSharedFlow<EventDto>()

        firestoreReference
            .collection(Collections.events)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    for (change in snapshot.documentChanges) {
                        if (change.type == DocumentChange.Type.MODIFIED && change.document.id == eventId) {
                            try {
                                val event = change.document.toObject(EventDto::class.java)
                                firestoreScope.launch {
                                    snapshots.emit(event)
                                }
                            } catch (e: RuntimeException) {
                                // no-op
                            }
                        }
                    }
                }
            }

        return snapshots.asSharedFlow()
    }
}
