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

package me.bakumon.moneykeeper.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import me.bakumon.moneykeeper.database.entity.AssetsTransferRecord

/**
 * AssetsTransferRecordDao
 *
 * @author Bakumon https://bakumon.me
 */
@Dao
interface AssetsTransferRecordDao {

    @Query("SELECT * FROM AssetsTransferRecord WHERE state=0 AND (assets_id_form=:id OR assets_id_to=:id) ORDER BY time DESC, create_time DESC")
    fun getTransferRecordsById(id: Int): LiveData<List<AssetsTransferRecord>>

    @Insert
    fun insertTransferRecord(vararg assetsTransferRecord: AssetsTransferRecord)
}