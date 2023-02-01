package com.ars.technologies.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ars.technologies.myapplication.databinding.FragmentReportsBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportsFragment : Fragment() {
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var button_date1: Button? = null
    var textview_date1: TextView? = null

    var button_date2: Button? = null
    var textview_date2: TextView? = null

    var submit_button: Button? = null

    var report_button: Button? = null

    var codeSpinner: Spinner? = null


    var reportType: String = ""

    var cal = Calendar.getInstance()

    private val reportViewModel by activityViewModels<ReportsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_reports, container, false)
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        val view = binding.root
//        val view= inflater.inflate(R.layout.fragment_transaction, container, false)
//        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val application = requireNotNull(this.activity).application
        System.out.println("!!!!!!!!!!!!!" + application)
        val dao = InventoryDatabase.getInstance(application).transactionsDAO
        System.out.println("!!!!!!!!!!!!!dao" + dao)
        val viewModelFactory = ReportsViewModelFactory(dao)
        System.out.println("!!!!!!!!!!!!!viewModelFactory" + viewModelFactory)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ReportsViewModel::class.java)
        System.out.println("!!!!!!!!!!!!!viewModel" + viewModel)
        binding.viewModel = viewModel
//        System.out.println("22222222222" + viewModel.transactionString)


        textview_date1 = view?.findViewById<View>(R.id.text_view_date_1) as TextView
        button_date1 = view?.findViewById<View>(R.id.button_date_1) as Button
        textview_date2 = view?.findViewById<View>(R.id.text_view_date_2) as TextView
        button_date2 = view?.findViewById<View>(R.id.button_date_2) as Button
        submit_button = view?.findViewById<View>(R.id.button_submit) as Button

        report_button = view?.findViewById<View>(R.id.button_report) as Button

        var table_Layout = view?.findViewById<View>(R.id.table_main) as TableLayout
        var table_title = view?.findViewById<View>(R.id.table_title) as TextView


        textview_date1!!.text = "--/--/----"

        textview_date2!!.text = "--/--/----"

        val reportGrp = view?.findViewById<View>(R.id.reportType) as? RadioGroup

        reportGrp?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = view?.findViewById<View>(checkedId) as RadioButton
            Toast.makeText(
                    activity?.applicationContext, " On checked change :" +
                    " ${radio.text}",
                    Toast.LENGTH_SHORT
            ).show()

            reportType = radio.text as String

            if (reportType == "Code Based Filter") {
                codeSpinner = view?.findViewById<View>(R.id.spinner_code) as? Spinner

                // get active doc ids from DB
                var codes: MutableList<String> = mutableListOf<String>()
                val masterDAO = InventoryDatabase.getInstance(application).masterDAO
                var masters = masterDAO.getAll()
                codes.add("Select a Code")
                for (master in masters) {
                    codes.add(master.code)
                }
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        codes
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                codeSpinner?.adapter = arrayAdapter

            }


        })


//        fun getCSVFileName(): String = "MoviesRoomExample.csv"


//        fun generateFile(context: Context, fileName: String): File? {
////            val csvFile = File(context.filesDir, fileName)
//            val csvFile = File(
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                    fileName
//            )
//            csvFile.createNewFile()
//
//            return if (csvFile.exists()) {
//                csvFile
//            } else {
//                null
//            }
//        }

//        fun goToFileIntent(context: Context, file: File): Intent {
//            val intent = Intent(Intent.ACTION_VIEW)
//            val contentUri = FileProvider.getUriForFile(
//                    context,
//                    "${context.packageName}.fileprovider",
//                    file
//            )
//            val mimeType = context.contentResolver.getType(contentUri)
//            intent.setDataAndType(contentUri, mimeType)
//            intent.flags =
//                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//
//            return intent
//        }

        var tableData = view.findViewById<View>(R.id.table_main) as TableLayout



        report_button!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                try {
                    var text = ""
                    System.out.println("tableData.getChildCount():" + tableData.getChildCount())

                    for (i in 0 until tableData.getChildCount()) {
                        // Get the one `courtText` in this rowv

                        var tableRow: TableRow = tableData.getChildAt(i) as TableRow
                        System.out.println(tableRow)

                        System.out.println("tableRow.childCount"+tableRow.childCount)

                        for (j in 0 until tableRow.childCount) {
                            System.out.println(tableRow.getChildAt(j))
                            if (tableRow.getChildAt(j) is Button) {
                                val button = tableRow.getChildAt(j) as Button
                                System.out.println("button"+button.text)
                                text= text + (button.text)
                            }else  if (tableRow.getChildAt(j) is TextView) {
                                val textView = tableRow.getChildAt(j) as TextView
                                System.out.println("textView"+textView.text)
                                text= text +(textView.text)
                            }
                            text= text + (",")
                        }
                        text= text + (System.lineSeparator())
                    }

                    val today = SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().time)

                    //saving the file into device

                    //saving the file into device


//                    System.out.println("Writing to file tableData:" + tableData)
                    val out: FileOutputStream = activity!!.openFileOutput("Data_$today.csv", Context.MODE_PRIVATE)
//                    var textview = tableData?.getChildAt(1) as? TextView
//                    val text: String = textview?.getText().toString()
                    System.out.println("Writing to file" + text)
                    out.write(text.toByteArray())
                    //out.write("test".toByteArray())
                    out.close()

                    //exporting

                    //exporting

                    val filelocation: File = File(activity!!.filesDir, "Data_$today.csv")
                    val path = FileProvider.getUriForFile(context!!, "com.ars.technologies.myapplication.fileprovider", filelocation)
                    val fileIntent = Intent(Intent.ACTION_SEND)
                    fileIntent.type = "text/csv"
                    val to = arrayOf("test@test.com")
                    fileIntent.putExtra(Intent.EXTRA_EMAIL, to)
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data_$today.csv")
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path)


                    startActivity(Intent.createChooser(fileIntent, "Send email"))


/*
                    val HEADER = "Name, DateTime"

                    var filename = "export.csv"

                    var path = activity?.getExternalFilesDir(null)   //get file directory for this package
//(Android/data/.../files | ... is your app package)

//create fileOut object
                    var fileOut = File(path, filename)

//delete any file object with path and filename that already exists
                    fileOut.delete()

//create a new file
                    fileOut.createNewFile()

//append the header and a newline
                    fileOut.appendText(HEADER)
                    fileOut.appendText("\n")
// trying to append some data into csv file
                    fileOut.appendText("Haider, 12/01/2021")
                    fileOut.appendText("\n")


                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileOut))
                    sendIntent.type = "text/csv"
                    startActivity(Intent.createChooser(sendIntent, "SHARE"))

*/


                    /* if (Build.VERSION.SDK_INT >= 23) {
                        val PERMISSIONS = arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        if (!hasPermissions(context, *PERMISSIONS)) {
                            ActivityCompat.requestPermissions(
                                (context as Activity?)!!,
                                PERMISSIONS,
                                targetRequestCode
                            )
                        } else {
                            //do here
                        }
                    } else {
                        //do here
                    }
*/
/*                    val csvFile = generateFile(activity!!.applicationContext, getCSVFileName())
                    System.out.println("csvFile : " + csvFile)
                    if (csvFile != null) {
                        csvWriter().open(csvFile, append = false) {
                            // Header
                            writeRow(listOf("[id]", "[CODE]", "[Document]"))
                            System.out.println(
                                "writeRow : " + listOf(
                                    "[id]",
                                    "[CODE]",
                                    "[Document]"
                                )
                            )
//                            moviesList.forEachIndexed { index, movie ->
//                                val directorName: String = moviesViewModel.directorsList.value?.find { it.id == movie.directorId }?.fullName ?: ""
//                                writeRow(listOf(index, movie.title, directorName))
//                            }
                        }

//                        Toast.makeText(this, "CSV File Generated", Toast.LENGTH_LONG).show()
                        val intent = context?.let { goToFileIntent(it, csvFile) }
//                        startActivity(intent)
                    } else {
//                        Toast.makeText(this, getString("CSV File Not Generated"), Toast.LENGTH_LONG).show()
                    }

*/
//
//                    FileOutputStream("filename.csv").apply {
//                        val writer = bufferedWriter()
//                        writer.write(""""Year", "Score", "Title"""")
//                        writer.newLine()
////                        movies.forEach {
////                            writer.write("${it.year}, ${it.score}, \"${it.title}\"")
////                            writer.newLine()
////                        }
//                        writer.flush()}

//                    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(fileUri, "r", null)
//                    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//                    val file = File(context.cacheDir, getFileName(context.contentResolver, fileUri))
//                    val outputStream = FileOutputStream(file)
//                    IOUtils.copy(inputStream, outputStream)

//                    org.apache.commons.io.FileUtils.copyInputStreamToFile(`is`, file)

//                    HSSFWorkbook().use { workbook ->
//                        val firstSheet = workbook.createSheet("FIRST SHEET")
//
//
//                        val rowA = firstSheet.createRow(0)
//                        val cellA = rowA.createCell(0)
//                        cellA.setCellValue(HSSFRichTextString("FIRST SHEET"))
//                        var path = Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_DOWNLOADS);
//                        val file = File(path, "CreateExcelDemo.xls")
//                        path.mkdirs();
//                        try {
//                            FileOutputStream(file).use { fos -> workbook.write(fos) }
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        submit_button!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var transactions: List<Transactions> = emptyList()
                System.out.println("reportType :::::::::" + reportType)
                if (reportType == "") {
                    //  no Item Selected
                    val text =
                            " You need to choose the Report Type as either Material In / Material Out"
                    Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
                } else {

                }
                System.out.println("Text ::::::" + reportType + "::")



                System.out.println("Search Start Time " + textview_date1!!.text + " Search End Time" + textview_date2!!.text + "Report Type " + reportType + "::");
                try {

                    if (reportType == "Material Out" || reportType == "Material In") {
                        val application = requireNotNull(activity?.applicationContext)
                        val dao = InventoryDatabase.getInstance(application).transactionsDAO

                        val fromDate = getEpochFromDatestr(textview_date1!!.text.toString())

                        val toDate = getEpochFromDatestr(textview_date2!!.text.toString())

                        val endDate = getEpochNextFromDatestr(textview_date2!!.text.toString())

                        System.out.println("fromDate:" + fromDate + " toDate:" + endDate + "reportType:" + reportType + "::");

                        transactions = dao.getTransactionByParams(fromDate, endDate, reportType)
//                    ReportsRepository(application,fromDate, toDate, reportType)


                        System.out.println("Transactiosn from DAO :" + transactions)

                        init(table_title, table_Layout, transactions, reportType)
                    } else if (reportType == "Stock Ledger") {

                        val application = requireNotNull(activity?.applicationContext)
                        val transactionDAO =
                                InventoryDatabase.getInstance(application).transactionsDAO
                        val masterDAO = InventoryDatabase.getInstance(application).masterDAO

                        val fromDate = getEpochFromDatestr(textview_date1!!.text.toString())
                        val toDate = getEpochNextFromDatestr(textview_date2!!.text.toString())

                        System.out.println("fromDate:" + fromDate + " toDate:" + toDate + "reportType:" + reportType + "::");

                        transactions = transactionDAO.getTransactionForSummary(fromDate, toDate)
                        var masters = masterDAO.getAll()
                        table_Layout?.removeAllViews()

                        table_title.text = "Stock Ledger (summary)"

                        val tbrow0 = TableRow(activity?.applicationContext)
                        val llp: TableRow.LayoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        )
                        llp.setMargins(0, 0, 2, 0)
                        val tv0 = Button(activity?.applicationContext)
                        tv0.text = " Sl.No  "
                        tv0.setTextColor(Color.BLACK)
                        tv0.setClickable(false)
//                        tbrow0.addView(tv0)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv1 = Button(activity?.applicationContext)
                        tv1.text = " Date "
                        tv1.setClickable(false)
                        tv1.setTextColor(Color.BLACK)
                        tbrow0.addView(tv1)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv2 = Button(activity?.applicationContext)
                        tv2.text = " Leather Code "
                        tv2.setClickable(false)
                        tv2.setTextColor(Color.BLACK)
                        tbrow0.addView(tv2)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv3 = Button(activity?.applicationContext)
                        tv3.text = " Description of Leather"
                        tv3.setTextColor(Color.BLACK)
                        tv3.setClickable(false)
                        tbrow0.addView(tv3)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv4 = Button(activity?.applicationContext)
                        tv4.text = " Unit"
                        tv4.setTextColor(Color.BLACK)
                        tv4.setClickable(false)
                        tbrow0.addView(tv4)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv5 = Button(activity?.applicationContext)
                        tv5.text = " Op Bal"
                        tv5.setTextColor(Color.BLACK)
                        tv5.setClickable(false)
                        tbrow0.addView(tv5)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv6 = Button(activity?.applicationContext)
                        tv6.text = " Received"
                        tv6.setTextColor(Color.BLACK)
                        tv6.setClickable(false)
                        tbrow0.addView(tv6)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv7 = Button(activity?.applicationContext)
                        tv7.text = " Issued"
                        tv7.setTextColor(Color.BLACK)
                        tv7.setClickable(false)
                        tbrow0.addView(tv7)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv8 = Button(activity?.applicationContext)
                        tv8.text = " Cl Balance"
                        tv8.setTextColor(Color.BLACK)
                        tv8.setClickable(false)
                        tbrow0.addView(tv8)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        table_Layout?.addView(tbrow0)

                        var recievedhashmap = hashMapOf<String, Double>()
                        var issuedhashmap = hashMapOf<String, Double>()
                        for (mast in masters) {
                            var recieved = 0.0
                            var issued = 0.0
                            for (transaction in transactions) {
                                if (mast.code.equals(transaction.code)) {
                                    if (getShortDate(mast.transactionDate.time) == getShortDate(
                                                    transaction.transactionDate.time
                                            )
                                    ) {
                                        if (transaction.transactionType == "Material In") {
                                            recieved = recieved + transaction.quantity
                                            recievedhashmap.put(mast.code, recieved);
                                        } else if (transaction.transactionType == "Material Out") {
                                            issued = issued + transaction.quantity
                                            issuedhashmap.put(mast.code, issued);
                                        }
                                    }
                                }
                            }
                        }
                        var addedMasters: MutableList<Master> = ArrayList()
                        var addedTransactions: MutableSet<Transactions> =
                                mutableSetOf<Transactions>()
                        for (mast in masters) {
                            run lit@{
                                for (transaction in transactions) {
                                    if (mast.code.equals(transaction.code)) {
                                        if (getShortDate(mast.transactionDate.time) == getShortDate(
                                                        transaction.transactionDate.time
                                                )
                                        ) {
                                            System.out.println(
                                                    "Comparing master date :" + getShortDate(
                                                            mast.transactionDate.time
                                                    ) + " vs Transaction Date " + getShortDate(
                                                            transaction.transactionDate.time
                                                    )
                                            )
                                            for (transe in addedTransactions) {
                                                if (transe.code == transaction.code) {
                                                    return@lit
                                                }
                                            }


                                            System.out.println("Code: " + mast.code + "Description: " + mast.description + "MasterId:" + mast.masterId + "Quantity :" + mast.quantity + "Rate:" + mast.rate + "Unit:" + mast.unit + "Transaction Date:" + mast.transactionDate)
                                            val tbrow = TableRow(activity?.applicationContext)
                                            val t1v = TextView(activity?.applicationContext)
                                            t1v.text = getShortDate(mast.transactionDate.time)
                                            t1v.setTextColor(Color.BLACK)
                                            t1v.gravity = Gravity.CENTER
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t1v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t2v = TextView(activity?.applicationContext)
                                            t2v.text = mast.code
                                            t2v.setTextColor(Color.BLACK)
                                            t2v.gravity = Gravity.CENTER
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t2v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t3v = TextView(activity?.applicationContext)
                                            t3v.text = mast.description
                                            t3v.setTextColor(Color.BLACK)
                                            t3v.gravity = Gravity.LEFT
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t3v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t4v = TextView(activity?.applicationContext)
                                            t4v.text = mast.unit
                                            t4v.setTextColor(Color.BLACK)
                                            tbrow.setLayoutParams(llp)
                                            t4v.gravity = Gravity.CENTER
                                            tbrow.addView(t4v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t5v = TextView(activity?.applicationContext)
                                            t5v.text = mast.quantity.toString()
                                            t5v.setTextColor(Color.BLACK)
                                            t5v.gravity = Gravity.CENTER
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t5v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t6v = TextView(activity?.applicationContext)
                                            if (recievedhashmap.get(mast.code) != null) {
                                                t6v.text = recievedhashmap.get(mast.code).toString()
                                            } else {
                                                t6v.text = "0"
                                            }
                                            t6v.setTextColor(Color.BLACK)
                                            t6v.gravity = Gravity.CENTER
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t6v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t7v = TextView(activity?.applicationContext)
                                            if (issuedhashmap.get(mast.code) != null) {
                                                t7v.text = issuedhashmap.get(mast.code).toString()
                                            } else {
                                                t7v.text = "0"
                                            }
                                            t7v.setTextColor(Color.BLACK)
                                            t7v.gravity = Gravity.CENTER
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t7v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            val t8v = TextView(activity?.applicationContext)
                                            System.out.println("Code:" + mast.code)
                                            System.out.println("Quantity :" + mast.quantity)
                                            if (issuedhashmap.get(mast.code) != null && recievedhashmap.get(
                                                            mast.code
                                                    ) == null
                                            ) {
                                                System.out.println(
                                                        "Issued Quantity:" + issuedhashmap.get(
                                                                mast.code
                                                        )
                                                )
                                                t8v.text =
                                                        (mast.quantity - issuedhashmap.get(mast.code)!!).toString()
                                            } else
                                                if (recievedhashmap.get(mast.code) != null && issuedhashmap.get(
                                                                mast.code
                                                        ) == null
                                                ) {
                                                    System.out.println(
                                                            "Recieved Quantity :" + recievedhashmap.get(
                                                                    mast.code
                                                            )!!
                                                    )
                                                    t8v.text =
                                                            (mast.quantity + recievedhashmap.get(
                                                                    mast.code
                                                            )!!).toString()
                                                } else if (recievedhashmap.get(mast.code) != null && issuedhashmap.get(
                                                                mast.code
                                                        ) != null
                                                ) {
                                                    t8v.text =
                                                            (mast.quantity - issuedhashmap.get(mast.code)!! + recievedhashmap.get(
                                                                    mast.code
                                                            )!!)!!.toString()
                                                }

//                                    t8v.text = mast.quantity.toString()
                                            t8v.setTextColor(Color.BLACK)
                                            t8v.gravity = Gravity.CENTER
                                            tbrow.setLayoutParams(llp)
                                            tbrow.addView(t8v)
                                            tbrow0.setPadding(2, 0, 2, 0)
                                            table_Layout?.addView(tbrow)
                                            addedMasters.add(mast)
                                            addedTransactions.add(transaction)

                                        }
                                    }

                                }

//
                            }

                        }

                        // add the non transacting masters
                        var rematiningMaster: MutableList<Master> = masters as MutableList<Master>

                        for (master in addedMasters) {
                            rematiningMaster.remove(master)
                        }
                        for (mast in rematiningMaster) {
                            val tbrow = TableRow(activity?.applicationContext)
                            val t1v = TextView(activity?.applicationContext)
                            t1v.text = getShortDate(mast.transactionDate.time)
                            t1v.setTextColor(Color.BLACK)
                            t1v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t1v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t2v = TextView(activity?.applicationContext)
                            t2v.text = mast.code
                            t2v.setTextColor(Color.BLACK)
                            t2v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t2v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t3v = TextView(activity?.applicationContext)
                            t3v.text = mast.description
                            t3v.setTextColor(Color.BLACK)
                            t3v.gravity = Gravity.LEFT
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t3v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t4v = TextView(activity?.applicationContext)
                            t4v.text = mast.unit
                            t4v.setTextColor(Color.BLACK)
                            tbrow.setLayoutParams(llp)
                            t4v.gravity = Gravity.CENTER
                            tbrow.addView(t4v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t5v = TextView(activity?.applicationContext)
                            t5v.text = mast.quantity.toString()
                            t5v.setTextColor(Color.BLACK)
                            t5v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t5v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t6v = TextView(activity?.applicationContext)
                            if (recievedhashmap.get(mast.code) != null) {
                                t6v.text = recievedhashmap.get(mast.code).toString()
                            } else {
                                t6v.text = "0"
                            }
                            t6v.setTextColor(Color.BLACK)
                            t6v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t6v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t7v = TextView(activity?.applicationContext)
                            if (issuedhashmap.get(mast.code) != null) {
                                t7v.text = issuedhashmap.get(mast.code).toString()
                            } else {
                                t7v.text = "0"
                            }
                            t7v.setTextColor(Color.BLACK)
                            t7v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t7v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t8v = TextView(activity?.applicationContext)
                            System.out.println("Code:" + mast.code)
                            System.out.println("Quantity :" + mast.quantity)
                            t8v.text = mast.quantity.toString()
                            t8v.setTextColor(Color.BLACK)
                            t8v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t8v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            table_Layout?.addView(tbrow)
                        }
                        System.out.println("Transactiosn from DAO :" + transactions)


                    } else if (reportType == "Code Based Filter") {
                        table_Layout?.removeAllViews()
                        table_title.text = "Bin Card"
                        System.out.println(" Vignesh Code based filter")
                        val fromDate = getEpochFromDatestr(textview_date1!!.text.toString())
                        val toDate = getEpochNextFromDatestr(textview_date2!!.text.toString())

                        val selectedCode: String = codeSpinner?.getSelectedItem().toString()
                        System.out.println(" Vignesh Code based filter : selected Code " + selectedCode)
                        val transactionDAO =
                                InventoryDatabase.getInstance(application).transactionsDAO

                        val masterDAO =
                                InventoryDatabase.getInstance(application).masterDAO

                        val master = masterDAO.get(selectedCode)

                        System.out.println(
                                " Entry Date:: "
                                        + master?.transactionDate + "Code::"
                                        + master?.code + " Description : "
                                        + master?.description
                                        + " Quantity "
                                        + master?.quantity
                                        + " Unit"
                                        + master?.unit
                        )


//                        for(transaction in transactions) {
//
//                            System.out.println(
//                                " Entry Date:: " + transaction.transactionDate +" Doc Id::" + transaction.documentId + " Trn Type : " + transaction.transactionType + " Code " + transaction.code + " Qty" + transaction.quantity+" unit "+transaction.unit)
//                        }
                        val tbrow0 = TableRow(activity?.applicationContext)
                        val llp: TableRow.LayoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        )
                        llp.setMargins(0, 0, 2, 0)
                        val tv0 = Button(activity?.applicationContext)
                        tv0.text = " Sl.No  "
                        tv0.setTextColor(Color.BLACK)
                        tv0.setClickable(false)
                        tbrow0.addView(tv0)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv1 = Button(activity?.applicationContext)
                        tv1.text = " Date "
                        tv1.setClickable(false)
                        tv1.setTextColor(Color.BLACK)
                        tbrow0.addView(tv1)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv2 = Button(activity?.applicationContext)
                        tv2.text = " Doc No"
                        tv2.setTextColor(Color.BLACK)
                        tv2.setClickable(false)
                        tbrow0.addView(tv2)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv3 = Button(activity?.applicationContext)
                        tv3.text = " Recieved"
                        tv3.setClickable(false)
                        tv3.setTextColor(Color.BLACK)
                        tbrow0.addView(tv3)
                        tbrow0.setLayoutParams(llp)
//                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv4 = Button(activity?.applicationContext)
                        tv4.text = " Issued"
                        tv4.setClickable(false)
                        tv4.setTextColor(Color.BLACK)
                        tbrow0.addView(tv4)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        val tv5 = Button(activity?.applicationContext)
                        tv5.text = " Balance"
                        tv5.setClickable(false)
                        tv5.setTextColor(Color.BLACK)
                        tbrow0.addView(tv5)
                        tbrow0.setLayoutParams(llp)
                        tbrow0.setPadding(2, 0, 2, 0)
                        table_Layout?.addView(tbrow0)
                        var i = 1
                        var runningBalance = 0.0

                        /**
                         * Compute the opening balance based on master and previous transaction dates
                         */

                        var masterQuantity = master.quantity
                        System.out.println("Starting row Creation masterQuantity " + masterQuantity)
                        System.out.println("Checking for transactions before " + fromDate + "for the code " + selectedCode)
                        val prevTxn = transactionDAO.getPrevTxn(selectedCode, fromDate)
                        System.out.println("prevTxn ::" + prevTxn)
                        for (transaction in prevTxn) {
                            if (transaction.transactionType == "Material In") {
                                masterQuantity = masterQuantity + transaction.quantity
                                System.out.println("Adding material in to masterQuantity " + transaction.quantity + " masterquantity:" + masterQuantity)
                            } else if (transaction.transactionType == "Material Out") {
                                masterQuantity = masterQuantity - transaction.quantity
                                System.out.println("Subtracting material out to masterQuantity " + transaction.quantity + " masterquantity:" + masterQuantity)
                            }
                        }


                        System.out.println("Checking for transactions with code :" + selectedCode + "fromDate : " + fromDate + "toDate" + toDate)
                        val transactions = transactionDAO.getTransactionForCodeAndDateRange(
                                selectedCode,
                                fromDate,
                                toDate
                        )
                        System.out.println("matching transactions ::" + transactions)


                        if (prevTxn.isEmpty() && transactions.isEmpty()) {

                            val master = masterDAO.get(selectedCode, fromDate, toDate)

                            val tbrow = TableRow(activity?.applicationContext)
                            val t1v = TextView(activity?.applicationContext)
                            t1v.text = i.toString()
                            t1v.setTextColor(Color.BLACK)
                            t1v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t1v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t2v = TextView(activity?.applicationContext)
                            t2v.text = getShortDate(master.transactionDate.time)
                            t2v.setTextColor(Color.BLACK)
                            t2v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t2v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t3v = TextView(activity?.applicationContext)
                            t3v.text = "NA"
                            t3v.setTextColor(Color.BLACK)
                            t3v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t3v)
                            tbrow0.setPadding(2, 0, 2, 0)

                            val t4v = TextView(activity?.applicationContext)
                            t4v.text = master.quantity.toString()
                            t4v.setTextColor(Color.BLACK)
                            tbrow.setLayoutParams(llp)
                            t4v.gravity = Gravity.CENTER
                            tbrow.addView(t4v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t5v = TextView(activity?.applicationContext)
                            t5v.text = "0.0"
                            t5v.setTextColor(Color.BLACK)
                            t5v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t5v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            masterQuantity = masterQuantity + master.quantity


                            val t6v = TextView(activity?.applicationContext)
                            t6v.text = master.quantity.toString()
                            t6v.setTextColor(Color.BLACK)
                            t6v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t6v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            table_Layout?.addView(tbrow)
                            System.out.println("Ending Row creation ")
                            i++


                        }

                        for (transaction in transactions) {
                            System.out.println(
                                    " Entry Date:: " + transaction.transactionDate + " Doc Id::" + transaction.documentId + " Trn Type : " + transaction.transactionType + " Code " + transaction.code + " Qty" + transaction.quantity + " unit " + transaction.unit
                            )

                            System.out.println("Starting row Creation")
                            val tbrow = TableRow(activity?.applicationContext)
                            val t1v = TextView(activity?.applicationContext)
                            t1v.text = i.toString()
                            t1v.setTextColor(Color.BLACK)
                            t1v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t1v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t2v = TextView(activity?.applicationContext)
                            t2v.text = getShortDate(transaction.transactionDate.time)
                            t2v.setTextColor(Color.BLACK)
                            t2v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t2v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            val t3v = TextView(activity?.applicationContext)
                            t3v.text = transaction.documentId
                            t3v.setTextColor(Color.BLACK)
                            t3v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t3v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            if (transaction.transactionType == "Material In") {
                                val t4v = TextView(activity?.applicationContext)
                                t4v.text = transaction.quantity.toString()
                                t4v.setTextColor(Color.BLACK)
                                tbrow.setLayoutParams(llp)
                                t4v.gravity = Gravity.CENTER
                                tbrow.addView(t4v)
                                tbrow0.setPadding(2, 0, 2, 0)
                                val t5v = TextView(activity?.applicationContext)
                                t5v.text = "0.0"
                                t5v.setTextColor(Color.BLACK)
                                t5v.gravity = Gravity.CENTER
                                tbrow.setLayoutParams(llp)
                                tbrow.addView(t5v)
                                tbrow0.setPadding(2, 0, 2, 0)
                                masterQuantity = masterQuantity + transaction.quantity
                            } else if (transaction.transactionType == "Material Out") {
                                val t4v = TextView(activity?.applicationContext)
                                t4v.text = "0.0"
                                t4v.setTextColor(Color.BLACK)
                                tbrow.setLayoutParams(llp)
                                t4v.gravity = Gravity.CENTER
                                tbrow.addView(t4v)
                                tbrow0.setPadding(2, 0, 2, 0)
                                val t5v = TextView(activity?.applicationContext)
                                t5v.text = transaction.quantity.toString()
                                t5v.setTextColor(Color.BLACK)
                                t5v.gravity = Gravity.CENTER
                                tbrow.setLayoutParams(llp)
                                tbrow.addView(t5v)
                                tbrow0.setPadding(2, 0, 2, 0)
                                masterQuantity = masterQuantity - transaction.quantity
                            }


                            val t6v = TextView(activity?.applicationContext)
                            t6v.text = masterQuantity.toString()
                            t6v.setTextColor(Color.BLACK)
                            t6v.gravity = Gravity.CENTER
                            tbrow.setLayoutParams(llp)
                            tbrow.addView(t6v)
                            tbrow0.setPadding(2, 0, 2, 0)
                            table_Layout?.addView(tbrow)
                            System.out.println("Ending Row creation ")
                            i++
                        }
                    }

//                    for (transaction in transactions) {
//                        print("Transaction from DB :" + transaction.transactionDate + transaction.unit + transaction.documentId + transaction.code + transaction.quantity)
//                    }
                } catch (ex: Exception) {
                    ex.printStackTrace();
                }


            }
        })


        // create an OnDateSetListener
        val dateSetListener1 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                    view: DatePicker, year: Int, monthOfYear: Int,
                    dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView1()
            }
        }

        val dateSetListener2 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                    view: DatePicker, year: Int, monthOfYear: Int,
                    dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView2()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date1!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                context?.let {
                    DatePickerDialog(
                            it,
                            dateSetListener1,
                            // set DatePickerDialog to point to today's date when it loads up
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }

        })

        button_date2!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                context?.let {
                    DatePickerDialog(
                            it,
                            dateSetListener2,
                            // set DatePickerDialog to point to today's date when it loads up
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }

        })

        return view
    }

    fun init(
            table_title: TextView,
            table_Layout: TableLayout?,
            transactions: List<Transactions>,
            reportType: String
    ) {
        System.out.println("Starting table creation ")

        table_Layout?.removeAllViews()
//        val stk = view?.findViewById(R.id.table_main) as? TableLayout

        if (reportType == "Material In") {
            table_title.text = "Details of Reciept"
        } else {
            table_title.text = "Details of Issued"

        }


        val tbrow0 = TableRow(activity?.applicationContext)
        val llp: TableRow.LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        )
        llp.setMargins(0, 0, 2, 0)
        val tv0 = Button(activity?.applicationContext)
        tv0.text = " Sl.No  "
        tv0.setTextColor(Color.BLACK)
        tv0.setClickable(false)
        tbrow0.addView(tv0)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv1 = Button(activity?.applicationContext)
        tv1.text = " Date "
        tv1.setClickable(false)
        tv1.setTextColor(Color.BLACK)
        tbrow0.addView(tv1)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv2 = Button(activity?.applicationContext)
        tv2.text = " DOC NO "
        tv2.setTextColor(Color.BLACK)
        tv2.setClickable(false)
        tbrow0.addView(tv2)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv3 = Button(activity?.applicationContext)
        tv3.text = " Leather Code"
        tv3.setTextColor(Color.BLACK)
        tv3.setClickable(false)
        tbrow0.addView(tv3)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv4 = Button(activity?.applicationContext)
        tv4.text = " Unit"
        tv4.setTextColor(Color.BLACK)
        tv4.setClickable(false)
        tbrow0.addView(tv4)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv5 = Button(activity?.applicationContext)
        tv5.text = " Qty"
        tv5.setClickable(false)
        tv5.setTextColor(Color.BLACK)
        tbrow0.addView(tv5)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        table_Layout?.addView(tbrow0)
        for (transaction in transactions) {
            System.out.println("Starting row Creation")
            val tbrow = TableRow(activity?.applicationContext)
            val t1v = TextView(activity?.applicationContext)
            t1v.text = transaction.transactionId.toString()
            t1v.setTextColor(Color.BLACK)
            t1v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t1v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t2v = TextView(activity?.applicationContext)
            t2v.text = getShortDate(transaction.transactionDate.time)
            t2v.setTextColor(Color.BLACK)
            t2v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t2v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t3v = TextView(activity?.applicationContext)
            t3v.text = transaction.documentId
            t3v.setTextColor(Color.BLACK)
            t3v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t3v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t4v = TextView(activity?.applicationContext)
            t4v.text = transaction.code
            t4v.setTextColor(Color.BLACK)
            tbrow.setLayoutParams(llp)
            t4v.gravity = Gravity.CENTER
            tbrow.addView(t4v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t5v = TextView(activity?.applicationContext)
            t5v.text = transaction.unit
            t5v.setTextColor(Color.BLACK)
            t5v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t5v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t6v = TextView(activity?.applicationContext)
            t6v.text = transaction.quantity.toString()
            t6v.setTextColor(Color.BLACK)
            t6v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t6v)
            tbrow0.setPadding(2, 0, 2, 0)
            table_Layout?.addView(tbrow)
            System.out.println("Ending Row creation ")
        }
        System.out.println("Ending table creation ")
    }

    fun initForSummary(
            table_title: TextView,
            table_Layout: TableLayout?,
            transactions: List<Transactions>,
            reportType: String
    ) {
        System.out.println("Starting table creation ")

        table_Layout?.removeAllViews()
//        val stk = view?.findViewById(R.id.table_main) as? TableLayout

        if (reportType == "Material In") {
            table_title.text = "Details of Reciept"
        } else {
            table_title.text = "Details of Issued"

        }


        val tbrow0 = TableRow(activity?.applicationContext)
        val llp: TableRow.LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        )
        llp.setMargins(0, 0, 2, 0)
        val tv0 = TextView(activity?.applicationContext)
        tv0.text = " Sl.No  "
        tv0.setTextColor(Color.BLACK)
        tbrow0.addView(tv0)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv1 = TextView(activity?.applicationContext)
        tv1.text = " Date "
        tv1.setTextColor(Color.BLACK)
        tbrow0.addView(tv1)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv2 = TextView(activity?.applicationContext)
        tv2.text = " Doc "
        tv2.setTextColor(Color.BLACK)
        tbrow0.addView(tv2)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv3 = TextView(activity?.applicationContext)
        tv3.text = " Doc No"
        tv3.setTextColor(Color.BLACK)
        tbrow0.addView(tv3)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv4 = TextView(activity?.applicationContext)
        tv4.text = " Unit"
        tv4.setTextColor(Color.BLACK)
        tbrow0.addView(tv4)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv5 = TextView(activity?.applicationContext)
        tv5.text = " Qty"
        tv5.setTextColor(Color.BLACK)
        tbrow0.addView(tv5)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        table_Layout?.addView(tbrow0)
        for (transaction in transactions) {
            System.out.println("Starting row Creation")
            val tbrow = TableRow(activity?.applicationContext)
            val t1v = TextView(activity?.applicationContext)
            t1v.text = transaction.transactionId.toString()
            t1v.setTextColor(Color.BLACK)
            t1v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t1v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t2v = TextView(activity?.applicationContext)
            t2v.text = getShortDate(transaction.transactionDate.time) + "--"
            t2v.setTextColor(Color.BLACK)
            t2v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t2v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t3v = TextView(activity?.applicationContext)
            t3v.text = transaction.documentId
            t3v.setTextColor(Color.BLACK)
            t3v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t3v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t4v = TextView(activity?.applicationContext)
            t4v.text = transaction.code
            t4v.setTextColor(Color.BLACK)
            tbrow.setLayoutParams(llp)
            t4v.gravity = Gravity.CENTER
            tbrow.addView(t4v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t5v = TextView(activity?.applicationContext)
            t5v.text = transaction.unit
            t5v.setTextColor(Color.BLACK)
            t5v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t5v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t6v = TextView(activity?.applicationContext)
            t6v.text = transaction.quantity.toString()
            t6v.setTextColor(Color.BLACK)
            t6v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t6v)
            tbrow0.setPadding(2, 0, 2, 0)
            table_Layout?.addView(tbrow)
            System.out.println("Ending Row creation ")
        }
        System.out.println("Ending table creation ")
    }


    private fun updateDateInView1() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat)
        textview_date1!!.text = sdf.format(cal.getTime())
    }

    private fun updateDateInView2() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat)
        textview_date2!!.text = sdf.format(cal.getTime())
    }

    private fun getEpochFromDatestr(datestr: String): Long {
//        val str = "Jun 13 2003 23:11:52.454 UTC"
        val df = SimpleDateFormat("dd/MM/yyyy")
        val date = df.parse(datestr)
        val epoch = date.time
        return epoch
    }

    private fun getEpochNextFromDatestr(datestr: String): Long {
//        val str = "Jun 13 2003 23:11:52.454 UTC"
        val df = SimpleDateFormat("dd/MM/yyyy")
        val date = df.parse(datestr)
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, 1)
        val epoch = cal.timeInMillis
        return epoch
    }

    fun getShortDate(ts: Long?): String {
        if (ts == null) return ""
        //Get instance of calendar
        val calendar = Calendar.getInstance(Locale.getDefault())
        //get current date from ts
        calendar.timeInMillis = ts
        //return formatted date
        return android.text.format.DateFormat.format("dd/MM/yy", calendar).toString()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val reportsView = view?.findViewById<RecyclerView>(R.id.reports_list)


        if (reportsView != null) {
            reportsView.layoutManager = LinearLayoutManager(activity)
        }

        val application = requireNotNull(this.activity).application
        val dao = InventoryDatabase.getInstance(application).transactionsDAO

        //     val reportsData  = dao.getTransactionByParams()

        //     System.out.println("Vignesh***" + reportsData)

//        for (i in 1..5) {
//            data.add(ItemsViewModel(R.drawable.bullet, "Item " + i))
//        }
//        val masterAdapter = MasterItemAdapter(reportsData)
//        val adapter = CustomAdapter(data)
//
//
//        val reportsAdapter:ReportsItemAdapter =ReportsItemAdapter(reportsData);
//        if(reportsView != null){
//            reportsView.adapter = reportsAdapter
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch(requestCode) {
//            case REQUEST : {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //do here
//                } else {
//                    Toast.makeText(
//                        mContext,
//                        "The app was not allowed to read your store.",
//                        Toast.LENGTH_LONG
//                    ).show();
//                }
//            }
//        }
//    }


    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                                context,
                                permission
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ReportsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}