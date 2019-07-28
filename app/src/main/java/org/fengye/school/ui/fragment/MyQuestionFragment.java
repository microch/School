package org.fengye.school.ui.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.fengye.school.R;
import org.fengye.school.adapter.QuestionAdapter;
import org.fengye.school.base.BaseFragment;
import org.fengye.school.databinding.FragmentAllQuestionBinding;
import org.fengye.school.model.bmob.Question;
import org.fengye.school.vm.AllQuestionViewModel;
import org.fengye.school.vm.MyQuestionViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQuestionFragment extends BaseFragment<FragmentAllQuestionBinding, MyQuestionViewModel> implements OnRefreshLoadMoreListener {


    private static final String TAG = "AllQuestionFragment";


    private QuestionAdapter adapter;



    public static MyQuestionFragment newInstance() {

        Bundle args = new Bundle();

        MyQuestionFragment fragment = new MyQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MyQuestionFragment() {
    }

    @Override
    protected MyQuestionViewModel createViewModel() {
        return ViewModelProviders.of(this,viewModelFactory).get(MyQuestionViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        binding.topBar.setTitle("我的提问");

        binding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        adapter = new QuestionAdapter();


    }

    @Override
    protected void initListener() {
        super.initListener();


        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recycleView.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshLoadMoreListener(this);

        viewModel.getNewLiveData().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> items) {
                adapter.setNewData(items);
                binding.refreshLayout.finishRefresh(0,true,!viewModel.isHasMoreData());
            }
        });
        viewModel.getMoreLiveData().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> items) {
                adapter.addData(items);
                binding.refreshLayout.finishLoadMore(0,true,!viewModel.isHasMoreData());
            }
        });



        adapter.setItemClickListener(new QuestionAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position, Question question) {
                startFragment(QuestionInfoFragment.newInstance(question));
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        binding.refreshLayout.autoRefresh();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_all_question;
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        viewModel.loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        viewModel.refresh();
    }
}
