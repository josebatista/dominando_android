package dominando.android.livros.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dominando.android.livros.model.Book

class FbRepository {

    private val fbAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = fbAuth.currentUser

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
                    collection.document(book.id)
                        .set(book, SetOptions.merge())
                }
                saveTask
                    .addOnSuccessListener {
                        value = true
                    }
                    .addOnFailureListener {
                        value = false
                    }
            }
        }
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