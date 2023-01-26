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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
internal class Firestore @Inject constructor() : Datastore {

    private val firestore = Firebase.firestore
    private val firestoreScope = CoroutineScope(Dispatchers.Default)

    override suspend fun getEvent(eventId: String): DatastoreResult<EventDto> = suspendCoroutine { continuation ->

        firestore
            .collection(Collections.events)
            .document(eventId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    continuation.resume(Success(requireNotNull(snapshot.toObject(EventDto::class.java))))
                } else {
                    continuation.resume(Failure(DocumentNotExistsException(snapshot.id)))
                }

            }
            .addOnFailureListener { exception ->
                continuation.resume(Failure(exception))
            }
    }

    override suspend fun getAllEvents(): DatastoreResult<List<EventDto>> = suspendCoroutine { continuation ->
        firestore
            .collection(Collections.events)
            .get()
            .addOnSuccessListener { snapshot ->
                continuation.resume(
                    Success(
                        snapshot.documents
                            .filter { it.exists() }
                            .map { requireNotNull(it.toObject(EventDto::class.java)) }
                    )
                )
            }
            .addOnFailureListener { exception ->
                continuation.resume(Failure(exception))
            }
    }

    override suspend fun getHomeTiles(eventId: String): DatastoreResult<List<HomeTileDto>> = suspendCoroutine { continuation ->
        firestore
            .collection(Collections.events)
            .document(eventId)
            .collection(Collections.homeTiles)
            .get()
            .addOnSuccessListener { snapshot ->
                continuation.resume(
                    Success(
                        snapshot.documents
                            .filter { it.exists() }
                            .map { requireNotNull(it.toObject(HomeTileDto::class.java)) }
                    )
                )
            }
            .addOnFailureListener { exception ->
                continuation.resume(Failure(exception))
            }
    }

    override suspend fun observeEvent(eventId: String): SharedFlow<EventDto> {
        val snapshots = MutableSharedFlow<EventDto>()

        firestore
            .collection(Collections.events)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    for (change in snapshot.documentChanges) {
                        if (change.type == DocumentChange.Type.MODIFIED && change.document.id == eventId) {
                            firestoreScope.launch {
                                snapshots.emit(change.document.toObject(EventDto::class.java))
                            }
                        }
                    }
                }
            }

        return snapshots.asSharedFlow()
    }
}
