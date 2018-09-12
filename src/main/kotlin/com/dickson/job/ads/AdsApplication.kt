package com.dickson.job.ads

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter
import java.math.BigDecimal

@SpringBootApplication
class AdsApplication {

    @Bean
    fun loadData(productRepository: ProductRepository, userRepository: UserRepository): CommandLineRunner =
            CommandLineRunner {
                val ad1 = Product("Classic", "Classic Ad", BigDecimal(269.99))
                val ad2 = Product("Standout", "Standout Ad", BigDecimal(322.99))
                val ad3 = Product("Premium", "Premium Ad", BigDecimal(394.99))
                productRepository.save(ad1)
                productRepository.save(ad2)
                productRepository.save(ad3)

                val user1 = User(name = "nike_user", promotion = Promotion.NIKE)
                val user2 = User(id= 999, name = "normal_user")
                userRepository.save(user1)
                userRepository.save(user2)
                val item1 = Item(name = "first", product = ad1)
                val item2 = Item(name = "second", product = ad2)

            }
}


fun main(args: Array<String>) {
    runApplication<AdsApplication>(*args)
}

@Configuration
class RepositoryConfig : RepositoryRestConfigurerAdapter() {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        config.exposeIdsFor(User::class.java)
    }
}
