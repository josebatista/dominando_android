package dominando.android.contatos

import android.content.ContentProviderOperation
import android.content.Context
import android.content.Intent
import android.content.OperationApplicationException
import android.graphics.Bitmap
import android.os.RemoteException
import android.provider.ContactsContract
import java.io.ByteArrayOutputStream

object ContactUtils {

    fun insertContact(context: Context, name: String, phoneNumber: String, address: String, photo: Bitmap) {
        // lista de operacoes que serao realizadas em batch
        val operation = ArrayList<ContentProviderOperation>()

        // armazenara o id interno do contato e servira para inserir os detalhes
        val backRefIndex = 0

        // associa o contato a conta-padrao do phoneNumber
        operation.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        // adiciona o nome do contato e alimenta o id
        operation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build()
        )

        // adiciona um edereco ao contato a partir do id
        operation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address)
                .build()
        )

        // associar o phoneNumber ao contato do tipo "Home"
        operation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                )
                .build()
        )

        // adiciona imagem ao contato
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 75, stream)
        operation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backRefIndex)
                .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                .build()
        )

        // Aplica o batch de inclusao
        try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operation)
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
        }
    }

    fun insertContactWithApp(context: Context, name: String, phoneNumber: String, email: String, address: String) {
        context.startActivity(Intent(ContactsContract.Intents.Insert.ACTION).apply {
            setType(ContactsContract.RawContacts.CONTENT_TYPE)
                .putExtra(ContactsContract.Intents.Insert.NAME, name)
                .putExtra(ContactsContract.Intents.Insert.EMAIL, email)
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.POSTAL, address)
        })
    }

}