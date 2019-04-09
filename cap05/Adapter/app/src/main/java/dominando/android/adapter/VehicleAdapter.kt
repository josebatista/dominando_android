package dominando.android.adapter

import android.content.Context
import android.content.res.TypedArray
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.item_vehicle.view.*

class VehicleAdapter(
    private val ctx: Context,
    private val vehicles: List<Vehicle>
) : BaseAdapter() {

    private val logos: TypedArray by lazy {
        ctx.resources.obtainTypedArray(R.array.logos)
    }

    override fun getItem(position: Int) = vehicles[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = vehicles.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //1 passo
        val vehicle = vehicles[position]

        //2 passo
        val holder: ViewHolder
        val row: View

        if (convertView == null) {
            Log.d(TAG, "View Nova -> position $position")
            row = LayoutInflater.from(ctx).inflate(R.layout.item_vehicle, parent, false)
            holder = ViewHolder(row)
            row.tag = holder
        } else {
            Log.d(TAG, "View Existente -> position $position")
            row = convertView
            holder = convertView.tag as ViewHolder
        }

        //3 passo
        holder.imgLogo.setImageDrawable(logos.getDrawable(vehicle.manufacturer))
        holder.txtModel.text = vehicle.model
        holder.txtYear.text = vehicle.year.toString()
        holder.txtFuel.text = ctx.getString(getFuel(vehicle))

        //4 passo
        return row
    }

    companion object {
        data class ViewHolder(val view: View) {
            val imgLogo: ImageView = view.imgLogo
            val txtModel: TextView = view.txtModel
            val txtYear: TextView = view.txtYear
            val txtFuel: TextView = view.txtFuel
        }

        private val TAG = VehicleAdapter::class.java.simpleName

    }

    @StringRes
    private fun getFuel(vehicle: Vehicle): Int =
        if (vehicle.gasoline && vehicle.ethanol) R.string.fuel_flex
        else if (vehicle.gasoline) R.string.fuel_gasoline
        else if (vehicle.ethanol) R.string.fuel_ethanol
        else R.string.fuel_invalid

}