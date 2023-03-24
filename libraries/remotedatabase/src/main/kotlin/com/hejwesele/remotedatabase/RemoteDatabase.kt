package com.hejwesele.remotedatabase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass

class RemoteDatabase @Inject constructor() {
    private val root = Firebase.database.reference
    private val firestoreScope = CoroutineScope(Dispatchers.Default)

    suspend fun <T : Any> observe(path: String, id: String, type: KClass<T>): Flow<Result<T>> = withContext(Dispatchers.IO) {
        val snapshots = MutableSharedFlow<Result<T>>()
        val reference = root.child(path).child(id)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                runCatching {
                    snapshot.getValue(type.java)
                }.onSuccess { item ->
                    if (item != null) {
                        firestoreScope.launch {
                            snapshots.emit(success(item))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // no-op
            }
        })

        snapshots.asSharedFlow()
    }

    suspend fun <T : Any> read(path: String, id: String, type: KClass<T>): Result<T?> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val reference = root.child(path).child(id)
            reference.get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(runCatching { snapshot.getValue(type.java) })
                }
                .addOnFailureListener { error ->
                    continuation.resume(failure(error))
                }
        }
    }

    suspend fun <T : Any> readAll(path: String, type: KClass<T>): Result<List<T>> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val reference = root.child(path)
            reference.get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(
                        runCatching {
                            snapshot.children.mapNotNull { it.getValue(type.java) }
                        }
                    )
                }
                .addOnFailureListener { error ->
                    continuation.resume(failure(error))
                }
        }
    }

    suspend fun <T : Any> write(path: String, item: T): Result<T> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val key = createNewEntry(path)
            key?.let {
                val reference = root.child("$path$it")
                item.addId(it)
                reference.setValue(item)
                    .addOnSuccessListener { continuation.resume(success(item)) }
                    .addOnFailureListener { error -> continuation.resume(failure(error)) }
            } ?: continuation.resume(failure(UnknownError("Failed to create new entry for element $item")))
        }
    }

    suspend fun <T> update(path: String, id: String, item: T): Result<T> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val reference = root.child(path).child(id)
            reference.setValue(item)
                .addOnSuccessListener { continuation.resume(success(item)) }
                .addOnFailureListener { error -> continuation.resume(failure(error)) }
        }
    }

    suspend fun delete(path: String, id: String): Result<String> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val reference = root.child(path).child(id)
            reference.removeValue()
                .addOnSuccessListener { continuation.resume(success(id)) }
                .addOnFailureListener { error -> continuation.resume(failure(error)) }
        }
    }

    private fun createNewEntry(path: String): String? {
        return root.child(path).push().key
    }

    private fun <T : Any> T.addId(id: String) {
        this::class.java.declaredFields
            .firstOrNull { field ->
                field.isAnnotationPresent(AutoGeneratedId::class.java)
            }?.let { field ->
                if (field.type.isAssignableFrom(String::class.java)) {
                    field.isAccessible = true
                    field.set(this, id)
                } else {
                    throw IllegalArgumentException("Only property of type String can be declared as @AutoGeneratedId.")
                }
            }
    }
}