package com.bachhuberdesign.deckbuildergwent.features.factionselect

import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.changehandler.FlipChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.helpers.ClickListenerHelper
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.controller_faction_select.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class FactionSelectController : Controller(), FactionSelectMvpContract {

    companion object {
        @JvmStatic val TAG: String = FactionSelectController::class.java.name
    }

    @Inject
    lateinit var presenter: FactionSelectPresenter

    private var recyclerView: RecyclerView? = null
    private var adapter: FastItemAdapter<FactionItem>? = null
    private var isLeaderClickable: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_faction_select)
        activity?.title = "Select Faction"

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        initRecyclerView(view)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)

        if (adapter?.adapterItems?.size == 0) {
            presenter.loadFactions()
        }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    private fun initRecyclerView(v: View) {
        adapter = FastItemAdapter()
        adapter!!.withItemEvent(object : ClickEventHook<FactionItem>() {
            override fun onBindMany(viewHolder: RecyclerView.ViewHolder): MutableList<View>? {
                if (viewHolder is FactionItem.ViewHolder) {
                    return ClickListenerHelper.toList(viewHolder.leader1, viewHolder.leader2, viewHolder.leader3)
                } else {
                    return super.onBindMany(viewHolder)
                }
            }

            override fun onClick(view: View, i: Int, fastAdapter: FastAdapter<FactionItem>, item: FactionItem) {
                if (isLeaderClickable) {
                    isLeaderClickable = false
                    val index = Integer.valueOf(view.tag as String)
                    beginCardFlipAnimation(view as ImageView, item.leaders!![index])
                }
            }
        })

        val layoutManager = LinearLayoutManager(activity)

        recyclerView = v.recycler_view
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter
    }

    private fun beginCardFlipAnimation(imageView: ImageView, leader: Card) {
        val flip1 = AnimationUtils.loadAnimation(activity, R.anim.card_flip_1)
        val flip2 = AnimationUtils.loadAnimation(activity, R.anim.card_flip_2)

        flip1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                Glide.with(activity)
                        .load(Uri.parse("file:///android_asset/faction_back_${leader.faction}_thumbnail_padded.png"))
                        .fitCenter()
                        .dontAnimate()
                        .into(imageView)
                imageView.animation = flip2
                imageView.startAnimation(flip2)
            }
        })

        flip2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                onLeaderSelected(leader)
            }
        })

        imageView.animation = flip1
        imageView.startAnimation(flip1)
    }

    override fun onFactionsLoaded(factions: List<Faction>) {
        factions.forEach { faction ->
            val item: FactionItem = FactionItem()
            item.factionName = faction.name
            item.factionDescription = faction.effect
            item.backgroundUrl = faction.iconUrl
            item.leaders = faction.leaders
            adapter?.add(item)
        }

        adapter?.notifyDataSetChanged()
    }

    override fun onLeaderSelected(leader: Card) {
        router.pushController(RouterTransaction.with(LeaderConfirmController(leader))
                .pushChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.RIGHT))
                .popChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.LEFT)))

        isLeaderClickable = true
    }

}