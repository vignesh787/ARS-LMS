package com.ars.technologies.myapplication


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.ars.technologies.myapplication.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragement) as NavHostFragment
        val navController = navHostFragment.navController
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val builder = AppBarConfiguration.Builder(navController.graph)
        builder.setDrawerLayout(drawer)
        val appBarConfiguration = builder.build()
        toolbar.setupWithNavController(navController,appBarConfiguration)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        NavigationUI.setupWithNavController(navView,navController)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragement)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode,resultCode,data)
//       System.out.println("Vignesh:" + resultCode)
//            if (resultCode == RESULT_OK) {
//                val uri = data.getData();
//                System.out.println("Vignesh123" + uri.toString())
//                val dataStream = contentResolver.openInputStream(uri!!)
//
//                val isr =  InputStreamReader(dataStream)
//                val br =  BufferedReader(isr)
//                var read: Int
//                val buffer = CharArray(4096)
//                var line = br.readLine()
//                while(line != null){
//                    System.out.println("********"+line)
//                    line = br.readLine()
//                }
//
//            }
//        }


//     override fun onClick(v: View) {
//            val intent = Intent()
//            //Use the proper Intent action
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                intent.action = Intent.ACTION_OPEN_DOCUMENT
//            } else {
//                intent.action = Intent.ACTION_GET_CONTENT
//            }
//            //Only return files to which we can open a stream
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            when (v.id) {
//                R.id.csvButton -> {
//                    intent.type = "*/*"
//                    startActivityForResult(intent, 1)
//                    return
//                }
//
//                else -> return
//            }
//        }






}
