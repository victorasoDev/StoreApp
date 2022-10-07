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
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "productId")
    val productId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "price")
    val price: Long,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "wishlistId")
    val wishlistId: Long,
    val addDate: Long
)

fun CartProductEntity.asExternalModel() = CartProduct(
    id = productId,
    name = name,
    imageUrl = imageUrl,
    price = price,
    category = category,
    wishlistId = wishlistId,
    addDate = addDate
)
