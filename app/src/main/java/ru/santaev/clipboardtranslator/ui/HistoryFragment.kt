package ru.santaev.clipboardtranslator.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.*
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.databinding.FragmentHistoryBinding
import ru.santaev.clipboardtranslator.ui.adapter.HistoryAdapter
import ru.santaev.clipboardtranslator.util.Analytics
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_DELETE_ALL_HISTORY
import ru.santaev.clipboardtranslator.util.AnalyticsConstants.EVENT_ID_NAME_CLICK_SWIPE_DELETE
import ru.santaev.clipboardtranslator.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var analytics: Analytics
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Analytics(requireContext())

        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        adapter = HistoryAdapter(null)
        binding.historyList.adapter = adapter
        binding.historyList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        adapter.listener = viewModel::onClickedItem
        val simpleItemTouchCallback = HistoryAdapter.RecyclerViewCallback(
                onSwipedListener = { position ->
                    analytics.logClickEvent(EVENT_ID_NAME_CLICK_SWIPE_DELETE)
                    viewModel.removeItem(adapter.getTranslation(position))
                })

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.historyList)
        binding.historyList.setHasFixedSize(false)
        observeViewModel()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete_all -> {
                analytics.logClickEvent(EVENT_ID_NAME_CLICK_DELETE_ALL_HISTORY)
                viewModel.clearHistory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        viewModel.history.observe(this, Observer { translations ->
            if (translations != null && !translations.isEmpty()) {
                adapter.setTranslations(translations)
                adapter.notifyDataSetChanged()
                binding.isEmpty = false
            } else {
                binding.isEmpty = true
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}