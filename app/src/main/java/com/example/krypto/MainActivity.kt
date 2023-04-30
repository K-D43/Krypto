package com.example.krypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.krypto.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvModelAdapter
    private lateinit var data:ArrayList<Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        apiData
        data=ArrayList<Model>()
        rvAdapter=RvModelAdapter(this,data)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=rvAdapter

        binding.search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val filterData=ArrayList<Model>()
                for (item in data){
                    if (item.name.lowercase(Locale.getDefault()).contains(s.toString().lowercase(Locale.getDefault()))){
                        filterData.add(item)
                    }

                }
                if (filterData.isEmpty()){
                    Toast.makeText(this@MainActivity,"No data available",Toast.LENGTH_SHORT).show()
                }else{
                    rvAdapter.changeData(filterData)
                }
            }


        })

    }

    val apiData:Unit
    get() {
        val url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

        val queue= Volley.newRequestQueue(this)
        val jsonObjectRequest:JsonObjectRequest=
            object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                response ->
                binding.progressBar.isVisible=false
                try {
                    val dataArray=response.getJSONArray("data")
                    for (i in 0 until dataArray.length()){
                        val dataObject=dataArray.getJSONObject(i)
                        val symbol=dataObject.getString("symbol")
                        val name=dataObject.getString("name")
                        val quote=dataObject.getJSONObject("quote")
                        val USD=quote.getJSONObject("USD")
                        val price = String.format("$ "+"%.2f",USD.getDouble("price"))
                        data.add(Model(name,symbol,price.toString()))
                    }
                    rvAdapter.notifyDataSetChanged()
                }catch (e:Exception){
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                }
            },Response.ErrorListener {
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            })
            {
                override fun getHeaders(): Map<String, String> {
                    val headers=HashMap<String,String>()
                    headers["X-CMC_PRO_API_KEY"]="fa05d924-da7e-4bc1-95ca-f996f81118a6"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }

}