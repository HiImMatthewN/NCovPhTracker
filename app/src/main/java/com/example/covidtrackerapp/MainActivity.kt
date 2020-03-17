package com.example.covidtrackerapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),OnNotesButtonClick{
    private lateinit var fullList:ArrayList<CaseObject>
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_RV.adapter = adapter
        by_area_Btn.visibility = View.VISIBLE
        setOnClickListeners()
        loadPersonCaseJSON()
    }

    private fun setOnClickListeners() {

        by_area_Btn.setOnClickListener {
            adapter.clear()
            loadPlaceCaseJSON()


        }
        by_person_Btn.setOnClickListener {
            adapter.clear()

            loadPersonCaseJSON()


        }
        val spinnerAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.filter_choices_array))
        filter_Spinner.adapter = spinnerAdapter
        filter_Spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
              when(id){
                  0L -> loadFullAll()
                  1L-> filterById()
                  2L -> filterByStatus()
                  3L -> filterByAge()
                  4L -> filterByDate()
                  5L-> filterByGender()
                  6L -> filterByNationality()


              }
            }
        }


    }
    private fun loadFullAll(){
        try{
            adapter.clear()
           loadPlaceCaseJSON()
        }catch (e:Exception){
            //

        }

    }
    private fun filterById(){
        val builder =AlertDialog.Builder(this)
        builder.setTitle(R.string.searchMenu_ID_itemString)
            .setItems(R.array.SearchMenuID) { dialog, which ->
                when(which){
                    0 -> {
                        val input =EditText(this)
                        input.inputType = InputType.TYPE_CLASS_NUMBER
                        val idBuilder = AlertDialog.Builder(this)

                        input.hint = "Enter Case No."
                        idBuilder.setView(input).setPositiveButton("Search"){_,_ ->
                            val numInput = input.text.toString().trim().toInt()

                            if(numInput > fullList.size || numInput<0){
                                Toast.makeText(this,
                                    "Case Number does not exists",Toast.LENGTH_SHORT).show()
                                return@setPositiveButton
                            }
                            adapter.clear()
                            val forFilterList = fullList
                            for(case:CaseObject in forFilterList){
                                if(case.case_no.toInt() == numInput){
                                    adapter.add(CaseObjectRow(case,this))
                                }
                            }
                        }
                        idBuilder.create().show()
                    }
                    1->{
                        val inflater = LayoutInflater.from(this)
                            .inflate(R.layout.dialog_filter_by_id,null)
                        val startIdET = inflater.findViewById<EditText>(R.id.filterByIDStart_ET)
                        val endIdET = inflater.findViewById<EditText>(R.id.filterByIDEnd_ET)
                        val dialogBuilder = AlertDialog.Builder(this)
                        dialogBuilder.setTitle("Specify Start and End ID")
                        dialogBuilder.setPositiveButton("Search"
                        ) { _, _ ->
                            try {


                                val startId = startIdET.text.trimStart().trim().toString().toInt()
                                val endId = endIdET.text.trimStart().trim().toString().toInt()
                                adapter.clear()
                                val filterList = fullList
                                for (case: CaseObject in filterList) {
                                    if (case.case_no.toInt() in startId..endId) {
                                        adapter.add(CaseObjectRow(case, this))
                                    }
                                }

                            }catch (e:Exception){
                                Toasty.error(this,
                                    "Invalid Input"
                                    ,Toast.LENGTH_LONG,false).show()
                            }

                        }
                        dialogBuilder.setView(inflater).create().show()
                    }
                }
            }

        builder.create().show()

    }
    private fun filterByGender(){
        val builder =AlertDialog.Builder(this)
        builder.setTitle(R.string.searchMenu_gender_itemString)
            .setItems(R.array.SearchMenuGender) { dialog, which ->
                when(which){
                    0 -> {
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.gender == "Male"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.error(this,
                            "Number of Male cases: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()
                    }
                    1->{
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.gender == "Female"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.error(this,
                            "Number of Female cases: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()
                    }

                }
            }

        builder.create().show()


    }
    private fun filterByAge(){
        val builder =AlertDialog.Builder(this)
        builder.setTitle(R.string.searchMenu_age_itemString)
            .setItems(R.array.SearchMenuAge) { dialog, which ->
                when(which){
                    0 -> {
                        val input = EditText(this)
                        input.inputType = InputType.TYPE_CLASS_NUMBER
                        val ageFilterBuilder = AlertDialog.Builder(this)

                        input.hint = "Enter Age"
                        ageFilterBuilder.setView(input).setPositiveButton("Search") { _, _ ->
                            val numInput = input.text.toString().trim().toInt()
                            if (numInput < 0) {
                                Toast.makeText(
                                    this,
                                    "Invalid Age", Toast.LENGTH_SHORT
                                ).show()
                                return@setPositiveButton
                            }
                            adapter.clear()
                            val filterList = fullList
                            for (case: CaseObject in filterList) {
                                if (case.age.toInt() == numInput) {
                                    adapter.add(CaseObjectRow(case, this))
                                }
                            }
                            Toasty.success(
                                this,
                                "Number of cases at Age $numInput: ${adapter.itemCount}"
                                , Toast.LENGTH_LONG, false
                            ).show()
                        }

                        ageFilterBuilder.create().show()
                    }
                    1->{
                      val inflater = LayoutInflater.from(this)
                          .inflate(R.layout.dialog_filter_by_age,null)
                        val startAgeET = inflater.findViewById<EditText>(R.id.filterByAgeStart_ET)
                        val endAgeET = inflater.findViewById<EditText>(R.id.filterByAgeEnd_ET)
                        val dialogBuilder = AlertDialog.Builder(this)
                        dialogBuilder.setTitle("Specify Start and End Age")
                        dialogBuilder.setPositiveButton("Search"
                        ) { _, _ ->
                            try {


                                val startAge = startAgeET.text.trimStart().trim().toString().toInt()
                                val endAge = endAgeET.text.trimStart().trim().toString().toInt()
                                adapter.clear()
                                val filterList = fullList
                                for (case: CaseObject in filterList) {
                                    if (case.age.toInt() in startAge..endAge) {
                                        adapter.add(CaseObjectRow(case, this))
                                    }
                                }
                                Toasty.success(
                                    this,
                                    "Number of cases between Age $startAge-$endAge: ${adapter.itemCount}"
                                    , Toast.LENGTH_LONG, false
                                ).show()
                            }catch (e:Exception){
                                Toasty.error(this,
                                    "Invalid Input"
                                    ,Toast.LENGTH_LONG,false).show()
                            }

                        }
                        dialogBuilder.setView(inflater).create().show()
                    }
                }

            }

        builder.create().show()


    }
    private fun filterByStatus(){
        val builder =AlertDialog.Builder(this)
        builder.setTitle(R.string.searchMenu_status_itemString)
            .setItems(R.array.SearchMenuStatus) { dialog, which ->
                when(which){
                    0 -> {
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.status == "Admitted"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.warning(this,
                            "Number of Admitted: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()

                    }
                    1->{
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.status == "Recovered"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.success(this,
                            "Number of Recovered: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()
                    }
                    2->{
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.status == "Died"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.error(this,
                            "Number of death: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()

                    }


                }


            }

        builder.create().show()



    }
    private fun filterByNationality(){
        val builder =AlertDialog.Builder(this)
        builder.setTitle(R.string.searchMenu_nationality_itemString)
            .setItems(R.array.SearchMenuNationality) { dialog, which ->
                when(which){
                    0 -> {
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.nationality == "Filipino"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.success(this,
                            "Number of Filipinos cases: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()

                    }
                    1->{
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.nationality != "Filipino" && case.nationality != "TBA"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.success(this,
                            "Foreigner cases: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()
                    }
                    2->{
                        adapter.clear()
                        val filterList = fullList
                        for(case:CaseObject in filterList){
                            if(case.nationality == "TBA"){
                                adapter.add(CaseObjectRow(case,this))
                            }
                        }
                        Toasty.success(this,
                            "TBA of Nationality cases: ${adapter.itemCount}"
                            ,Toast.LENGTH_LONG,false).show()
                    }

                }

            }

        builder.create().show()


    }
    private fun filterByDate(){
        val builder =AlertDialog.Builder(this)
        builder.setTitle(R.string.searchMenu_Date_itemString)
            .setItems(R.array.SearchMenuDate) { dialog, which ->
                when(which){
                    0 -> {
                        val inflater = LayoutInflater.from(this)
                            .inflate(R.layout.dialog_filter_by_date_specifc,null)
                      val datePicker = inflater.findViewById<DatePicker>(R.id.filter_date_specific_DP)
                        val dialogBuilder = AlertDialog.Builder(this)
                        dialogBuilder.setTitle("Choose Date")
                        dialogBuilder.setPositiveButton("Search"
                        ) { _, _ ->
                            val year = datePicker.year.toString()
                            val rawMonth = datePicker.month + 1
                            var month = rawMonth.toString()
                            var dayOfMonth = datePicker.dayOfMonth.toString()
                                if(month.toInt() < 10)
                                    month = "0$month"
                            if(dayOfMonth.toInt() <10)
                                dayOfMonth = "0$dayOfMonth"
                            val dateToSearch = "$year-$month-$dayOfMonth"
                            adapter.clear()
                            val filterList = fullList
                            for(case:CaseObject in filterList){
                                if(case.date == dateToSearch){
                                    adapter.add(CaseObjectRow(case,this))
                                }
                            }


                        }
                        dialogBuilder.setView(inflater).create().show()
                    }
                    1->{
                    showDPStart()
                    }
                }
            }

        builder.create().show()
    }
    private fun showDPStart(){
        val inflater = LayoutInflater.from(this)
            .inflate(R.layout.dailog_filter_by_date_start,null)
        val datePicker = inflater.findViewById<DatePicker>(R.id.filter_date_start_DP)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Choose Date(Start)")
        dialogBuilder.setPositiveButton("Next"
        ) { dialog, _ ->
            val year = datePicker.year.toString()
            val rawMonth = datePicker.month + 1
            var month = rawMonth.toString()
            var dayOfMonth = datePicker.dayOfMonth.toString()
            if(month.toInt() < 10)
                month = "0$month"
            if(dayOfMonth.toInt() <10)
                dayOfMonth = "0$dayOfMonth"
           showDPEnd(month,dayOfMonth,year)
            dialog.dismiss()
        }
        dialogBuilder.setView(inflater).create().show()

    }
    private fun showDPEnd(monthStart:String,dayOfMonthStart:String,yearStart:String){

        val inflater = LayoutInflater.from(this)
            .inflate(R.layout.dailog_filter_by_date_end,null)
        val datePicker = inflater.findViewById<DatePicker>(R.id.filter_date_end_DP)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Choose Date(End)")
        dialogBuilder.setPositiveButton("Search"
        ) { _, _ ->
            val yearEnd = datePicker.year.toString()
            val rawMonth = datePicker.month + 1
            var monthEnd = rawMonth.toString()
            var dayOfMonthEnd = datePicker.dayOfMonth.toString()
            if(monthEnd.toInt() < 10)
                monthEnd = "0$monthEnd"
            if(dayOfMonthEnd.toInt() <10)
                dayOfMonthEnd = "0$dayOfMonthEnd"
            adapter.clear()
            val filterList = fullList
            for(case:CaseObject in filterList){
                if(case.date.substring(0,4).toInt() <= yearEnd.toInt()
                    && case.date.substring(0,4).toInt() >= yearStart.toInt()){
                    if(case.date.substring(5,7).toInt() <= monthEnd.toInt()
                        && case.date.substring(5,7).toInt()>= monthStart.toInt()){
                        if(case.date.substring(8,10).toInt() <= dayOfMonthEnd.toInt()
                            && case.date.substring(8,10).toInt() >= dayOfMonthStart.toInt()){
                            adapter.add(CaseObjectRow(case,this))

                        }

                    }


                }


            }



        }
        dialogBuilder.setView(inflater).create().show()



    }
    private fun loadPersonCaseJSON() {
        by_area_Btn.isClickable = false
        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://coronavirus-ph-api.now.sh")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(CasesByPersonApi::class.java)
        val call = request.getCases()
        call.enqueue(object : Callback<ArrayList<CaseObject>> {
            override fun onFailure(call: Call<ArrayList<CaseObject>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                Toast.makeText(this@MainActivity, "Hello", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<ArrayList<CaseObject>>,
                response: Response<ArrayList<CaseObject>>
            ) {
                val jsonResponse = response.body() ?: return
                showData(jsonResponse)
                fullList = jsonResponse
                by_area_Btn.isClickable = true
                progressBar.visibility = View.GONE
                by_person_Btn.visibility = View.GONE
                by_area_Btn.visibility = View.VISIBLE
                filter_Spinner.visibility = View.VISIBLE


            }
        })

    }
    private fun loadPlaceCaseJSON(){
        by_person_Btn.isClickable = false
        progressBar.visibility = View.VISIBLE
        val retrofit = Retrofit.Builder()
            .baseUrl("https://script.googleusercontent.com/macros/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(CasesByPlaceApi::class.java)
        val call = request.getPlace()
        call.enqueue(object : Callback<AreaObject> {
            override fun onFailure(call: Call<AreaObject>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<AreaObject>,
                response: Response<AreaObject>
            ) {
                val jsonResponse = response.body() ?: return
                val getAreaList = jsonResponse.areas
                for(place:PlaceObject in getAreaList){
                    adapter.add(PlaceObjectRow(place))

                }
                by_person_Btn.isClickable = true
                progressBar.visibility = View.GONE
                by_area_Btn.visibility = View.GONE
                by_person_Btn.visibility = View.VISIBLE
                filter_Spinner.visibility = View.GONE

            }
        })


    }
    override fun onClick(note: String,caseNum:Int) {
            showNotesDialog(note,caseNum)
    }
    private fun showNotesDialog(note:String,caseNum: Int){
        val inflater = LayoutInflater.from(this).inflate(R.layout.dialog_note,null)
        val caseNumTV = inflater.findViewById<TextView>(R.id.caseNum_TV)
        val noteTV = inflater.findViewById<TextView>(R.id.noteDialog_TV)
        val dialog = Dialog(this)
        noteTV.text = note
        caseNumTV.text = "Case no: $caseNum"
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(inflater)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.show()


    }

    private fun showData(data: ArrayList<CaseObject>) {
        var recoveredCount = 0
        var admmittedCount = 0
        var diedCount = 0
        for (case: CaseObject in data) {
            if (case.status == "Recovered")
                recoveredCount++
            if (case.status == "Died")
                diedCount++
            if(case.status == "Admitted")
                admmittedCount++

            adapter.add(CaseObjectRow(case,this))
        }
        admitted_TV.text = admmittedCount.toString()
        recoveredNum_TV.text = recoveredCount.toString()
        died_TV.text = diedCount.toString()
        totalCases_TV.text = data.size.toString()

    }


}
