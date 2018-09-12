package com.dickson.job.ads

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
interface ProductRepository : PagingAndSortingRepository<Product, String>


@RepositoryRestResource(collectionResourceRel = "users", path = "users")
interface UserRepository : PagingAndSortingRepository<User, Long>


@RepositoryRestResource(collectionResourceRel = "items", path = "items")
interface ItemRepository : PagingAndSortingRepository<Item, Long>

@RepositoryRestResource(collectionResourceRel = "checkouts", path = "checkouts")
interface CheckoutRepository : PagingAndSortingRepository<Checkout, Long>
