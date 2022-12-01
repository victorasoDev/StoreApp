/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwu.victoraso.storeapp.room.model

import androidx.room.*
import uwu.victoraso.storeapp.model.CartProduct

/**
 * Defines an [CartProduct].
 * It is the child in a 1 to many relationship with [CartEntity]
 */
@Entity(
    tableName = "cart_products",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["id"],
            childColumns = ["cart_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["cart_id"])
    ]
)
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon_url")
    val iconUrl: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "price")
    val price: Long,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "cart_id")
    val cartId: Long,
    val addDate: Long
)

fun CartProductEntity.asExternalModel() = CartProduct(
    productId = productId,
    name = name,
    iconUrl = iconUrl,
    imageUrl = imageUrl,
    price = price,
    category = category,
    cartId = cartId,
    addDate = addDate
)
