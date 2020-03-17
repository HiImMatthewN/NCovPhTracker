package com.example.covidtrackerapp

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.place_row.view.*

class PlaceObjectRow(val place:PlaceObject):Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.place_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val regionTV = viewHolder.itemView.region_TV
        val cityProvinceTV = viewHolder.itemView.cityProvince_TV
        val confirmedCaseTV = viewHolder.itemView.confirmedCasesl_TV

        regionTV.text = place.region
        cityProvinceTV.text = place.city_province
        confirmedCaseTV.text = place.confirmed
    }
}