package dominando.android.livros.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dominando.android.livros.model.Book
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class FbRepository {

    private val fbAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = fbAuth.currentUser

    private val storageRef = FirebaseStorage.getInstance().reference.child(BOOKS_KEY)

    fun saveBook(book: Book): LiveData<Boolean> {
        return object : LiveData<Boolean>() {
            override fun onActive() {
                super.onActive()

                if (currentUser == null) {
                    throw SecurityException("Invalid User")
                }

                val collection = firestore.collection(BOOKS_KEY)
                val saveTask = if (book.id.isBlank()) {
                    book.userId = currentUser.uid
                    collection.add(book).continueWith { task ->
                        if (task.isSuccessful) {
                            book.id = task.result?.id ?: ""
                        }
                    }
                } else {
                    collection.document(book.id).set(book)
                }
                saveTask
                    .addOnSuccessListener {
                        if (book.coverUrl.startsWith("file://")) {
                            uploadFile()
                        } else {
                            value = true
                        }
                    }
                    .addOnFailureListener {
                        value = false
                    }
            }

            private fun uploadFile() {
                uploadPhoto(book).continueWithTask { urlTask ->
                    File(book.coverUrl).delete()
                    book.coverUrl = urlTask.result.toString()
                    firestore.collection(BOOKS_KEY).document(book.id).update(COVER_URL_KEY, book.coverUrl)
                }.addOnCompleteListener { task ->
                    value = task.isSuccessful
                }
            }
        }
    }

    private fun uploadPhoto(book: Book): Task<Uri> {
        compressPhoto(book.coverUrl)
        val storageRef = storageRef.child(book.id)
        return storageRef.putFile(Uri.parse(book.coverUrl))
            .continueWithTask { uploadTask -> uploadTask.result?.storage?.downloadUrl }
    }

    private fun compressPhoto(path: String) {
        val imgFile = File(path.substringAfter("file://"))
        val bos = ByteArrayOutputStream()
        val bmp = BitmapFactory.decodeFile(imgFile.absolutePath)
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos)

        val fos = FileOutputStream(imgFile)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
    }

//    Metodo simples utilizando get() que nao atualiza quando os dados sao modificados no servidor.
//    fun loadBooks(): LiveData<List<Book>> {
//        return object : LiveData<List<Book>>() {
//            override fun onActive() {
//                super.onActive()
//                firestore.collection(BOOKS_KEY)
//                    .whereEqualTo(USER_ID_KEY, currentUser?.uid)
//                    .get()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val books = task.result?.map { document ->
//                                val book = document.toObject(Book::class.java)
//                                book.id = document.id
//                                book
//                            }
//                            value = books
//                        } else {
//                            throw Exception(task.exception)
//                        }
//                    }
//            }
//        }
//    }

    fun loadBooks(): LiveData<List<Book>> {
        return object : LiveData<List<Book>>() {
            override fun onActive() {
                super.onActive()
                firestore.collection(BOOKS_KEY)
                    .whereEqualTo(USER_ID_KEY, currentUser?.uid)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if (firebaseFirestoreException == null) {
                            val books = querySnapshot?.map { document ->
                                val book = document.toObject(Book::class.java)
                                book.id = document.id
                                book
                            }
                            value = books
                        } else {
                            throw firebaseFirestoreException
                        }
                    }
            }
        }
    }

    fun loadBook(bookId: String): LiveData<Book> {
        return object : LiveData<Book>() {
            override fun onActive() {
                super.onActive()
                firestore.collection(BOOKS_KEY)
                    .document(bookId)
                    .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        if (firebaseFirestoreException == null) {
                            if (documentSnapshot != null) {
                                val book = documentSnapshot.toObject(Book::class.java)
                                book?.id = documentSnapshot.id
                                value = book
                            }
                        } else {
                            throw firebaseFirestoreException
                        }
                    }
            }
        }
    }

    fun remove(book: Book): LiveData<Boolean> {
        return object : LiveData<Boolean>() {
            override fun onActive() {
                super.onActive()
                firestore.collection(BOOKS_KEY)
                    .document(book.id)
                    .delete()
                    .addOnCompleteListener {
                        value = it.isSuccessful
                    }
            }
        }
    }

    companion object {
        const val BOOKS_KEY = "books"
        const val USER_ID_KEY = "userId"
        const val COVER_URL_KEY = "coverUrl"
    }


}