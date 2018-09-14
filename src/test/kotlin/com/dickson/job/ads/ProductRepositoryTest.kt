package com.dickson.job.ads

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@DataJpaTest
class ProductRepositoryTest(@Autowired val entityManager: TestEntityManager,
                            @Autowired val productRepository: ProductRepository) {
    @Test
    fun `When findAll then return all`() {
        val ad1 = Product("Classic", "Classic Ad", BigDecimal(269.99))
        val ad2 = Product("Standout", "Standout Ad", BigDecimal(322.99))
        val ad3 = Product("Premium", "Premium Ad", BigDecimal(394.99))
        productRepository.save(ad1)
        productRepository.save(ad2)
        productRepository.save(ad3)

        val found = productRepository.findAll().toList()
        assertThat(found.size).isEqualTo(3)
        assertThat(found).containsExactly(ad1, ad2, ad3)
    }
}

