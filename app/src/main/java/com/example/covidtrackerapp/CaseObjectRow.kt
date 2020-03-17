package com.example.covidtrackerapp

import android.graphics.Color
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.case_row.view.*

class CaseObjectRow(val case:CaseObject,val listener:OnNotesButtonClick):Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.case_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val caseNumTV = viewHolder.itemView.caseNum_TV
        val dateTV = viewHolder.itemView.date_TV
        val ageTV = viewHolder.itemView.age_TV
        val genderTV = viewHolder.itemView.gender_TV
        val nationalityTV = viewHolder.itemView.nationality_TV
        val admittionTV = viewHolder.itemView.hospital_TV
        val travelHistoryTV = viewHolder.itemView.travelHistory_TV
        val statusTV = viewHolder.itemView.status_TV
        val button = viewHolder.itemView.seeNotes_btn

        caseNumTV.text = case.case_no
        dateTV.text = case.date
        ageTV.text = case.age
        genderTV.text = case.gender
        nationalityTV.text = case.nationality
        admittionTV.text = case.hospital_admitted_to
        travelHistoryTV.text = case.had_recent_travel_history_abroad
        statusTV.text = case.status

        if(case.gender == "Male")
            genderTV.setTextColor(Color.parseColor("#32a1ff"))
        if(case.gender == "Female")
            genderTV.setTextColor(Color.parseColor("#ff5bb4"))
        if(case.status == "Died")
            statusTV.setTextColor(Color.parseColor("#FF0000"))
        if(case.status == "Recovered")
            statusTV.setTextColor(Color.parseColor("#66ff00"))
        if(case.status == "Admitted")
            statusTV.setTextColor(Color.parseColor("#ffaa1d"))


        //OnClick Button
        button.setOnClickListener {
            listener.onClick(case.notes,case.case_no.toInt())


        }

    }
}