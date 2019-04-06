package dominando.android.adapter

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
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
        val row = LayoutInflater.from(ctx).inflate(R.layout.item_vehicle, parent, false)

        //3 passo
        row.imgLogo.setImageDrawable(logos.getDrawable(vehicle.manufacturer))
        row.txtModel.text = vehicle.model
        row.txtYear.text = vehicle.year.toString()
        row.txtFuel.text = ctx.getString(getFuel(vehicle))

        //4 passo
        return row
    }

    @StringRes
    private fun getFuel(vehicle: Vehicle): Int =
        if (vehicle.gasoline && vehicle.ethanol) R.string.fuel_flex
        else if (vehicle.gasoline) R.string.fuel_gasoline
        else if (vehicle.ethanol) R.string.fuel_ethanol
        else R.string.fuel_invalid

}