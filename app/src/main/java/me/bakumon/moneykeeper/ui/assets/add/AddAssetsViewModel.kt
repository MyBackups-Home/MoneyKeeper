/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper.ui.assets.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.bakumon.moneykeeper.base.Resource
import me.bakumon.moneykeeper.database.entity.Assets
import me.bakumon.moneykeeper.database.entity.AssetsModifyRecord
import me.bakumon.moneykeeper.datasource.AppDataSource
import me.bakumon.moneykeeper.ui.common.BaseViewModel
import java.math.BigDecimal

/**
 * AddAssetsViewModel
 *
 * @author Bakumon https://bakumon.me
 */
class AddAssetsViewModel(dataSource: AppDataSource) : BaseViewModel(dataSource) {

    fun addAssets(assets: Assets): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        mDisposable.add(mDataSource.addAssets(assets)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                liveData.value = Resource.create(true)
            }
            ) { throwable ->
                throwable.printStackTrace()
                liveData.value = Resource.create(throwable)
            })
        return liveData
    }

    fun updateAssets(moneyBefore: BigDecimal, assets: Assets): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()
        mDisposable.add(mDataSource.updateAssets(assets)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (moneyBefore == assets.money) {
                    liveData.value = Resource.create(true)
                } else {
                    addAssetsModifyRecord(liveData, moneyBefore, assets)
                }
            }
            ) { throwable ->
                liveData.value = Resource.create(throwable)
            })
        return liveData
    }

    private fun addAssetsModifyRecord(
        liveData: MutableLiveData<Resource<Boolean>>,
        moneyBefore: BigDecimal,
        assets: Assets
    ): LiveData<Resource<Boolean>> {
        val modifyRecord = AssetsModifyRecord(assetsId = assets.id!!, moneyBefore = moneyBefore, money = assets.money)
        mDisposable.add(mDataSource.insertAssetsRecord(modifyRecord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                liveData.value = Resource.create(true)
            }
            ) { throwable ->
                liveData.value = Resource.create(throwable)
            })
        return liveData
    }

}
