package dominando.android.contatos

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactAdapter(context: Context, c: Cursor?) : CursorAdapter(context, c, 0) {

    lateinit var indexes: IntArray


    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup?): View {
        indexes = intArrayOf(
            cursor.getColumnIndex(ContactsContract.Contacts._ID),
            cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY),
            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        )

        return LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val txtName = view.txtName
        val qcBadge = view.qcbPhoto
        val contractUri = ContactsContract.Contacts.getLookupUri(
            cursor.getLong(indexes[0]),
            cursor.getString(indexes[1])
        )
        txtName.text = cursor.getString(indexes[2])
        qcBadge.assignContactUri(contractUri)
        Picasso.get()
            .load(contractUri)
            .placeholder(R.mipmap.ic_launcher)
            .into(qcBadge)
    }
}