package com.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.ShopItemActivity.Companion.newIntentEditItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

private lateinit var viewModel: MainViewModel
private lateinit var shopListAdapter: ShopListAdapter
private var shopItemContainer: FragmentContainerView? = null

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        // ViewModelProvider(this) даёт MainViewModel, привязанный к Activity,
        // чтобы он переживал переворот экрана и хранил данные
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        //Подписываешься на LiveData,
        // чтобы обновлять список на экране каждый раз, когда shopList изменится
        viewModel.shopListLiveData.observe(this) { list: List<ShopItem> ->
            shopListAdapter.submitList(list)
        }

        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    // Метод для определения режима работы("в одну колонку")
    private fun isOnePaneMode(): Boolean{
        return shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment){
        if (shopItemContainer!=null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.shop_item_container, fragment)
                .commit()
        }
    }

    // Настройка Списка
    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            shopListAdapter = ShopListAdapter(
                onShopItemClickListener =  { item: ShopItem -> setupClickListener(item) },
                onShopItemLongClickListener = {item: ShopItem -> setupLongClickListener(item)}
            )
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }

        setupSwipeListener(rvShopList)
    }

    // Свайр для удаления
    private fun setupSwipeListener(rvShopList: RecyclerView) {

        //Создаём ItemTouchHelper, который реагирует на свайпы влево и вправо
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            //Движение вверх/вниз отключено, поэтому возвращаем false.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        //Подключаем свайпы к RecyclerView
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
    // Обрабатываем клик по элементу списка
    private fun setupClickListener(item: ShopItem) {
        Log.d("MainActivity", item.toString())
        val intent = newIntentEditItem(this, item.id)
        startActivity(intent)
    }

    // Обрабатываем ДОЛГИЙ клик по элементу списка
    private fun setupLongClickListener(item: ShopItem) {
        viewModel.changeEnableState(item)
    }
}


//  Ненужная реализация списка через Linear Layout
//    private fun showList(list: List<ShopItem>){
//        llShopList.removeAllViews()
//        for (shopItem in list){
//            val layoutId = if (shopItem.enabled){
//                R.layout.item_shop_enabled
//            } else{
//                R.layout.item_shop_disabled
//            }
//            val view = LayoutInflater.from(this).inflate(layoutId, llShopList, false)
//            val tvName = view.findViewById<TextView>(R.id.tv_name)
//            val tvCount = view.findViewById<TextView>(R.id.tv_count)
//            tvName.text = shopItem.name
//            tvCount.text = shopItem.count.toString()
//            view.setOnLongClickListener {
//                viewModel.changeEnableState(shopItem)
//                true
//            }
//            llShopList.addView(view)
//        }
//    }
