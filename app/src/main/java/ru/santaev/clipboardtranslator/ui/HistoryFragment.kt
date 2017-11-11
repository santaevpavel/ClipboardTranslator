package ru.santaev.clipboardtranslator.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.FragmentHistoryBinding
import ru.santaev.clipboardtranslator.ui.adapter.HistoryAdapter
import ru.santaev.clipboardtranslator.util.Analytics
import ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_DELETE_ALL_HISTORY
import ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SWIPE_DELETE
import ru.santaev.clipboardtranslator.viewmodel.HistoryViewModel


class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    private lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Analytics(activity)
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        viewModel.history.observe(this, Observer { translations ->
            if (translations != null && !translations.isEmpty()) {
                adapter.setTranslations(translations)
                adapter.notifyDataSetChanged()

                binding.historyList.visibility = View.VISIBLE
                binding.emptyLayout.visibility = View.INVISIBLE
            } else {
                binding.historyList.visibility = View.INVISIBLE
                binding.emptyLayout.visibility = View.VISIBLE
            }
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_history, container, false)
        adapter = HistoryAdapter(null)
        binding.historyList.adapter = adapter
        binding.historyList.layoutManager = LinearLayoutManager(activity)
        adapter.setListener { translation -> viewModel.onClickedItem(translation) }
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                analytics.logClickEvent(EVENT_ID_NAME_CLICK_SWIPE_DELETE)
                viewModel.removeItem(adapter.getTranslation(position))
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.historyList)
        binding.historyList.setHasFixedSize(false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.fragment_history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.item_delete_all -> {
                analytics.logClickEvent(EVENT_ID_NAME_CLICK_DELETE_ALL_HISTORY)
                viewModel.clearHistory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}