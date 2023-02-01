package com.ars.technologies.myapplication

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ars.technologies.myapplication.databinding.FragmentTransactionBinding
import com.cipherlab.barcode.GeneralString
import com.cipherlab.barcode.ReaderManager
import com.cipherlab.barcode.decoder.BcReaderType
import com.cipherlab.barcodebase.ReaderCallback
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment(), View.OnClickListener, ReaderCallback {

    private var _binding:FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    var m_RM: ReaderManager? = null
    private var filter: IntentFilter? = null
    private var mReaderCallback: ReaderCallback? = null



    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
//        val view= inflater.inflate(R.layout.fragment_transaction, container, false)
//        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val application=requireNotNull(this.activity).application
        val dao = InventoryDatabase.getInstance(application).transactionsDAO

        val docIdSpinner = view?.findViewById<View>(R.id.spinner_docId) as Spinner

        val adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.docIds, android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        docIdSpinner.adapter = adapter;

        val unitsSpinner = view?.findViewById<View>(R.id.spinner_unit) as Spinner

        val unitsadapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.units, android.R.layout.simple_spinner_item)

        unitsadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        unitsSpinner.adapter = unitsadapter;

        m_RM = ReaderManager.InitInstance(context)
        mReaderCallback = this
        System.out.println("Hi there vignesh125: "+m_RM+" reader callback "+mReaderCallback)
      //  System.out.println("Hi there vignesh126: "+m_RM!!.GetReaderCallbackStatus())

        val code = view?.findViewById<View>(R.id.code)  as EditText

        code.setOnClickListener(View.OnClickListener {
            System.out.println("Hi there vignesh")

            m_RM?.SoftScanTrigger()


            val myDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    System.out.println("Hi there vignesh1::"+intent.action)
                    if (intent.action == GeneralString.Intent_READERSERVICE_CONNECTED) {
                        System.out.println("Hi there vignesh2")
                        val myReaderType: BcReaderType = m_RM!!.GetReaderType()
                        System.out.println("Hi there vignesh3")
                        code.setText(myReaderType.toString().trim())
                        System.out.println("Hi there vignesh4")
                        if (mReaderCallback != null) {
                            System.out.println("Hi there vignesh5")
                            m_RM!!.SetReaderCallback(mReaderCallback)
                        }
                    } else if (intent.action == GeneralString.Intent_SOFTTRIGGER_DATA) {
                        System.out.println("Hi there vignesh33")
                        val sDataStr = intent.getStringExtra(GeneralString.BcReaderData)
                        System.out.println("Hi there vignesh34::"+sDataStr)
                        if (sDataStr != null) {
                            code.setText(sDataStr.trim())
                        }
                        Toast.makeText(context, "Decoded data is $sDataStr",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }

            filter = IntentFilter()
            filter!!.addAction(GeneralString.Intent_READERSERVICE_CONNECTED)
            filter!!.addAction(GeneralString.Intent_SOFTTRIGGER_DATA)
            filter!!.addAction(GeneralString.Intent_PASS_TO_APP)



            requireActivity().registerReceiver(myDataReceiver, filter)

        })

//        code.setOnClickListener(){
//            System.out.println("INseide code onclick ")
//            val myDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//                override fun onReceive(context: Context?, intent: Intent) {
//                    System.out.println("intet.action"+intent.action)
//                    if (intent.action == GeneralString.Intent_READERSERVICE_CONNECTED) {
//// Make sure this app bind to barcode reader service , then user can use APIs
//// to get/set settings from barcode reader service
//                        val myReaderType: BcReaderType = m_RM!!.GetReaderType()
//                        code.setText(myReaderType.toString())
//                        if (mReaderCallback != null) {
//// Enable Callback function
//                            m_RM!!.SetReaderCallback(mReaderCallback)
//                        }
//                    }
//                }
//            }
//            filter = IntentFilter()
//            filter!!.addAction(GeneralString.Intent_SOFTTRIGGER_DATA)
//            requireActivity().registerReceiver(myDataReceiver, filter)
//        }



//        filter = IntentFilter()
//        filter!!.addAction(GeneralString.Intent_READERSERVICE_CONNECTED)
//        filter!!.addAction(GeneralString.Intent_SOFTTRIGGER_DATA)
//        filter!!.addAction(GeneralString.Intent_PASS_TO_APP)
//
//
//
//        requireActivity().registerReceiver(myDataReceiver, filter)

        val viewModelFactory = TransactionViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TransactionViewModel::class.java)
        binding.viewModel = viewModel

        val saveTransaction = view?.findViewById<View>(R.id.save_button) as Button

        saveTransaction.setOnClickListener(this)


        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                TransactionFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(p0: View?) {
        hideKeyboard()
        System.out.println("On Click INvoked !!")

        val application=requireNotNull(this.activity).application
        val dao = InventoryDatabase.getInstance(application).transactionsDAO
        val mySpinner = view?.findViewById<View>(R.id.spinner_docId) as Spinner
        val docIdStr: String = mySpinner.getSelectedItem().toString()
        val code = view?.findViewById<View>(R.id.code)  as EditText
        val codeStr = code.text.toString()
        val unitSpinner = view?.findViewById<View>(R.id.spinner_unit) as Spinner
        val unitStr: String = unitSpinner.getSelectedItem().toString()
        val qty = view?.findViewById<View>(R.id.quantity)  as EditText
        val qtyStr = qty.text.toString()
        val transactionGrp = view?.findViewById<View>(R.id.transactiontype) as RadioGroup
        val transactionType = transactionGrp.checkedRadioButtonId
        var transactoinTypeStr =""
        if(transactionType == -1){
            //  no Item Selected
            val text = " You need to choose the Transaction Type as either Material In / Material Out"
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
        }else{
            transactoinTypeStr = (when (transactionType){
                R.id.radio_materialin -> "Material In"
                else -> "Material Out"
            })
         System.out.println("Text ::::::" + transactoinTypeStr)


        }

        var viewModel = TransactionViewModel(dao)
        System.out.println("docid:" + docIdStr + "code:" + codeStr + "unit:" + unitStr + "quantity" + qtyStr + "txnType" + transactoinTypeStr)
        val transaction = Transactions(0, Date(), docIdStr, codeStr, unitStr, qtyStr.toDouble(), transactoinTypeStr)


        viewModel.addTransaction(transaction)
        val text =transactoinTypeStr + " Docid : " + docIdStr + " Code : " + codeStr + " Quantity : " + qtyStr + " Unit : " + unitStr + " Added Successfully";
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()


    }

    override fun asBinder(): IBinder {
        TODO("Not yet implemented")
    }

    override fun onDecodeComplete(p0: String?) {
        TODO("Not yet implemented")
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}