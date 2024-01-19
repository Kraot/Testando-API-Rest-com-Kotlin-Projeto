package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustumerServiceTest {
@MockK lateinit var costumerRepository: CustomerRepository
@InjectMockKs lateinit var customerService: CustomerService

@Test
fun `should create custumer`(){
    //given
val fakeCustomer: Customer = buildCustomer()
    every { costumerRepository.save(any()) } returns fakeCustomer
    //when
val actual:Customer = customerService.save(fakeCustomer)
    //then
    Assertions.assertThat(actual).isNotNull
    Assertions.assertThat(actual).isSameAs(fakeCustomer)
    verify (exactly = 1){ costumerRepository.save(fakeCustomer) }
}
    @Test
    fun `should find costumer by id`(){
        //given
    val fakeId: Long = Random(1).nextLong()
    val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { costumerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)


        //when
    val actual: Customer = customerService.findById(fakeId)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) {costumerRepository.findById(fakeId)  }

    }

    @Test
    fun `should not find costumer by id and thow BusinessException`(){

        // given
        val fakeId: Long = Random(1).nextLong()
        every { costumerRepository.findById(fakeId) } returns Optional.empty()

        // when
        //then
    Assertions.assertThatExceptionOfType(BusinessException::class.java)
        .isThrownBy {customerService.findById(fakeId) }
        .withMessage("Id $fakeId not found")
        verify(exactly = 1) { costumerRepository.findById(fakeId) }

    }

    @Test
    fun `should delete costumer by id `(){
        //given

        val fakeId: Long = java.util.Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { costumerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { costumerRepository.delete(fakeCustomer) } just runs

        //when
customerService.delete(fakeId)

        //then
        verify (exactly = 1){costumerRepository.findById(fakeId)  }
        verify (exactly = 1){costumerRepository.delete(fakeCustomer)  }

    }




private fun buildCustomer(
    firstName: String = "Matheus",
    lastName: String = "Ribeiro",
    cpf: String="166.876.568-99",
    email:String="matheuskraot@gmail.com",
    password:String="12345",
    zipCode:String="12345",
    street:String="Rua da João",
    income:BigDecimal = BigDecimal.valueOf(1000.0),
    id:Long= 1L
    ) = Customer(
      firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address (
            zipCode = zipCode,
            street =street,
        ),
        income =income,
        id = id

    )


}