package me.dio.credit.application.system.repository

import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
@Autowired lateinit var  creditRepository: CreditRepository
@Autowired lateinit var  testEntityManager: TestEntityManager

private lateinit var customer: Customer
private lateinit var credit1: Credit
private lateinit var credit2: Credit

@BeforeEach fun setup(){
    customer = testEntityManager.persist(buildCustomer())
    credit1 = testEntityManager.persist(buildCredit(customer=customer))
    credit2 = testEntityManager.persist(buildCredit(customer=customer))

}
    @Test
    fun `shoul find credit by credit code`(){
        //given
        val creditCode1 = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val creditCode2 = UUID.fromString("bb547c0f-9a6a-451f-8c89-afddce916a27")
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2

        //when
        val fakecredit1: Credit = creditRepository.findByCreditCode(creditCode1)!!
        val fakecredit2: Credit = creditRepository.findByCreditCode(creditCode2)!!

        //then
        Assertions.assertThat(fakecredit1).isNotNull
        Assertions.assertThat(fakecredit2).isNotNull
        Assertions.assertThat(fakecredit1).isSameAs(credit1)
        Assertions.assertThat(fakecredit2).isSameAs(credit2)



    }

    @Test
    fun `should find all credits by customer id`(){
        val customerId: Long = 1L

        val creditList: List< Credit> = creditRepository.findAllByCustomerId(customerId)

        Assertions.assertThat(creditList).isNotNull
        Assertions.assertThat(creditList.size).isEqualTo(2)
        Assertions.assertThat(creditList).contains(credit1,credit2)

    }


private fun buildCredit(
    creditValue: BigDecimal = BigDecimal.valueOf( 500.0),
    dayFirstInstallment: LocalDate = LocalDate.of(2023,Month.APRIL, 22),
    numberOfInstallments: Int = 5,
    customer: Customer
    ): Credit = Credit(
    creditValue = creditValue,
    dayFirstInstallment = dayFirstInstallment,
    numberOfInstallments = numberOfInstallments,
    customer = customer
    )




    private fun buildCustomer(
        firstName: String = "Matheus",
        lastName: String = "Ribeiro",
        cpf: String="166.876.568-99",
        email:String="matheuskraot@gmail.com",
        password:String="12345",
        zipCode:String="12345",
        street:String="Rua da João",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
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