package com.ars.technologies.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ars.technologies.myapplication.databinding.FragmentMasterBinding
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MasterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MasterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMasterBinding? =null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
//    private lateinit var binding: ResultProfileBinding
    private var param1: String? = null
    private var param2: String? = null
    var table_Layout = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ResultProfileBinding.inflate(layoutInflater)
//        val view = binding.root
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
//        val view = inflater.inflate(R.layout.fragment_master, container, false)
//
        _binding = FragmentMasterBinding.inflate(inflater,container,false)
//
        val view = binding.root
        val csvFile = view?.findViewById<View>(R.id.csvButton) as Button
        csvFile.setOnClickListener(this)



        val application= requireNotNull(this.activity).application
        val dao = InventoryDatabase.getInstance(application).masterDAO

        val viewModelFactory = MasterViewModelFactory(dao)

        val viewModel = ViewModelProvider(this,
                viewModelFactory).get(MasterViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner  = viewLifecycleOwner

//        val adapter =MasterItemAdapter()
//        binding.masterList.adapter=adapter

//        viewModel.masters.observe(viewLifecycleOwner, Observer{
//            it?.let{
//                adapter.data = it
//            }
//        })



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val masterView = view?.findViewById<RecyclerView>(R.id.master_list)
//        val recyclerview = view?.findViewById<RecyclerView>(R.id.recyclerview)
//        if (masterView != null) {
//            masterView.visibility  = View.INVISIBLE
//        };
//
//        if (masterView != null) {
//            masterView.layoutManager = LinearLayoutManager(activity)
//        }
//        if (recyclerview != null) {
//            recyclerview.layoutManager = LinearLayoutManager(activity)
//        }

        val application= requireNotNull(this.activity).application
        val dao = InventoryDatabase.getInstance(application).masterDAO

        val masterdata  = dao.getAll()

        System.out.println("Vignesh***"+masterdata)

        val data =ArrayList<ItemsViewModel>()

//        for (i in 1..5) {
//            data.add(ItemsViewModel(R.drawable.bullet, "Item " + i))
//        }
        val masterAdapter = MasterItemAdapter(masterdata)
        val adapter = CustomAdapter(data)

//        if (recyclerview != null) {
//            recyclerview.adapter = adapter
//        }

//        if(masterView != null){
//            masterView.adapter = masterAdapter
//        }

        val displayMasterData = view?.findViewById<View>(R.id.displayMaster) as Button


        val table_Layout = view?.findViewById<View>(R.id.table_main) as TableLayout

        displayMasterData!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val application= requireNotNull(activity).application
                val dao = InventoryDatabase.getInstance(application).masterDAO

                val masterdata  = dao.getAll()


                init(table_Layout,masterdata)



//                val masterView = view?.findViewById<RecyclerView>(R.id.master_list)
//                if(masterView!= null) {
//                    masterView.visibility = View.VISIBLE
//                }
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun init(
        table_Layout: TableLayout?,
        masters: List<Master>
    ) {
        System.out.println("Starting table creation ")

        table_Layout?.removeAllViews()
//        val stk = view?.findViewById(R.id.table_main) as? TableLayout

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
        tv2.text = " Leather Code "
        tv2.setTextColor(Color.BLACK)
        tv2.setClickable(false)
        tbrow0.addView(tv2)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv3 = Button(activity?.applicationContext)
        tv3.text = " Leather Description"
        tv3.setTextColor(Color.BLACK)
        tv3.setClickable(false)
        tbrow0.addView(tv3)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv4 = Button(activity?.applicationContext)
        tv4.text = " Qty"
        tv4.setTextColor(Color.BLACK)
        tv4.setClickable(false)
        tbrow0.addView(tv4)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        val tv5 = Button(activity?.applicationContext)
        tv5.text = " Units"
        tv5.setClickable(false)
        tv5.setTextColor(Color.BLACK)
        tbrow0.addView(tv5)
        tbrow0.setLayoutParams(llp)
        tbrow0.setPadding(2, 0, 2, 0)
        table_Layout?.addView(tbrow0)
        for (master in masters) {
            System.out.println("Starting row Creation")
            val tbrow = TableRow(activity?.applicationContext)
            val t1v = TextView(activity?.applicationContext)
            t1v.text = master.masterId.toString()
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
            t3v.text = master.code
            t3v.setTextColor(Color.BLACK)
            t3v.gravity = Gravity.LEFT
            tbrow.setLayoutParams(llp)
            tbrow.addView(t3v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t4v = TextView(activity?.applicationContext)
            t4v.text = master.description
            t4v.setTextColor(Color.BLACK)
            tbrow.setLayoutParams(llp)
            t4v.gravity = Gravity.LEFT
            tbrow.addView(t4v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t5v = TextView(activity?.applicationContext)
            t5v.text = master.quantity.toString()
            t5v.setTextColor(Color.BLACK)
            t5v.gravity = Gravity.CENTER
            tbrow.setLayoutParams(llp)
            tbrow.addView(t5v)
            tbrow0.setPadding(2, 0, 2, 0)
            val t6v = TextView(activity?.applicationContext)
            t6v.text = master.unit
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MasterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MasterFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(p0: View?) {
        val intent = Intent()
        //Use the proper Intent action
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.action = Intent.ACTION_OPEN_DOCUMENT
        } else {
            intent.action = Intent.ACTION_GET_CONTENT
        }
        //Only return files to which we can open a stream
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        when (p0?.id) {
            R.id.csvButton -> {
                intent.type = "*/*"
                startActivityForResult(intent, 1)
                return
            }

            else -> return
        }
    }

    fun getShortDate(ts: Long?):String{
        if(ts == null) return ""
        //Get instance of calendar
        val calendar = Calendar.getInstance(Locale.getDefault())
        //get current date from ts
        calendar.timeInMillis = ts
        //return formatted date
        return android.text.format.DateFormat.format("dd/MM/yy", calendar).toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)  {
        super.onActivityResult(requestCode, resultCode, data)
        System.out.println("Vignesh:" + resultCode)
        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.getData();
            System.out.println("Vignesh123" + uri.toString())
            val resolver = requireActivity().contentResolver
            val dataStream = resolver.openInputStream(uri!!)

            val isr =  InputStreamReader(dataStream)
            val br =  BufferedReader(isr)
//            var read: Int
//            val buffer = CharArray(4096)
            var line = br.readLine()
            var sNo : String;
            var code : String;
            var description : String;
            var unit : String;
            var quantity : String;
            var rate : String;
            var total : String

            var masters = ArrayList<Master>()
try{
            while(line != null){
//                System.out.println("********" + line)
                var entries = line . split (",")
                if(entries[1].equals("Code") )
                {
                    line = br.readLine()
                }
                entries = line . split (",")
                sNo = entries[0]
                code = entries[1]
                description=entries[2]
                unit=entries[3]
                quantity=entries[4]
                rate=entries[5]

//
//                if(code.equals("Total"))
//                {
//                    total = entries[5]
//                     break;
//                }

                System.out.println("sNo"+sNo)
                System.out.println("code"+code)
                System.out.println("description"+description)
                System.out.println("unit"+unit)
                System.out.println("quantity"+quantity)
                System.out.println("rate"+rate)
                if(sNo.isNotBlank()) {
                    var master = Master(
                       0L,
                        Date(),
                        code,
                        description,
                        unit,
                        quantity.toDouble(),
                        rate.toLong()
                    )

                    masters.add(master)
                }


                line = br.readLine()
            }
        val application=requireNotNull(this.activity).application
        val dao = InventoryDatabase.getInstance(application).masterDAO

        var masterViewModel = MasterViewModel(dao)
            val current = LocalDateTime.now()
    System.out.println("current"+current)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    System.out.println("formatter"+formatter)
            val formatted = current.format(formatter)
    System.out.println("formatted"+formatted)
        for(master in masters){
            System.out.println("master.code"+master.code)
            if(dao.get(master.code) != null){
                System.out.println("dao.get(master.code)"+dao.get(master.code))
               val master:Master =  dao.get(master.code)
                System.out.println("getShortDate(master.transactionDate.time) "+getShortDate(master.transactionDate.time))

               if(getShortDate(master.transactionDate.time) == formatted ) {
                   masterViewModel.updateMaster((master))
               }else{
                   masterViewModel.addMaster(master)
               }
            }else{
                masterViewModel.addMaster(master)
            }

        }


//           masterViewModel.addAll(masters)


        } catch(e:Exception){
                e.printStackTrace()
}
        }

    }
}