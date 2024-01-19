package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.dto.request.CustomerUpdateDto
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import kotlin.random.Random


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
   @Autowired  private lateinit var  customerRepository: CustomerRepository
    @Autowired  private lateinit var mockMvc: MockMvc
    @Autowired  private lateinit var objectMapper: ObjectMapper

    companion object{
        const val  URL: String = " /api/customers"
    }
    @BeforeEach fun setup() = customerRepository.deleteAll()
    @AfterEach fun tearDown() = customerRepository.deleteAll()

@Test
fun `Should create a custumer and return 201 status`(){

    //given
    val customerDto : CustomerDto = buildCustomerDto()
    val valueAsString : String = objectMapper.writeValueAsString(customerDto)

    // when
    mockMvc.perform(MockMvcRequestBuilders.post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(valueAsString))
        .andExpect(MockMvcResultMatchers.status().isCreated)
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Matheus"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Ribeiro"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("166.876.568-99"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("matheuskraot@gmail.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("12345"))
        .andDo(MockMvcResultHandlers.print()
    )
    //then


}

    @Test
    fun `should not save a customer with same CPF and Return 409 status`(){

        customerRepository.save(buildCustomerDto().toEntity())
        val customerDto : CustomerDto = buildCustomerDto()
        val valueAsString : String = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isConflict

        )
    }


    @Test
    fun `should not save a customer with fistName empty CPF and Return 400 status`(){
        val customerDto : CustomerDto = buildCustomerDto("")
        val valueAsString : String = objectMapper.writeValueAsString(customerDto)
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest

            )

    }

    @Test
    fun `should find customer by id and return 200 status`(){

       val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Matheus"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Ribeiro"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("166.876.568-99"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("matheuskraot@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("12345"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer whith invalid id  and return 400 status`(){
        val invalidId: Long =2L
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${invalidId}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)


    }

     @Test

     fun `should delete customer by id return 204 status`(){

        val customer:Customer= customerRepository.save(buildCustomerDto().toEntity())

         mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}")
             .accept(MediaType.APPLICATION_JSON))
             .andExpect(MockMvcResultMatchers.status().isNoContent)
             .andDo(MockMvcResultHandlers.print())
     }

    @Test

    fun `should not delete customer by id and return 400 status`(){

       val invalidId: Long = 2L

        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$${invalidId}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

@Test

fun `should update a customer and return 200 `(){
    val customer :Customer = customerRepository.save(buildCustomerDto().toEntity())
    val customerUpdateDtoDto: CustomerUpdateDto = buildCustomerUpdateDto()
    val valueAsString : String = objectMapper.writeValueAsString(customerUpdateDtoDto)

    mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
        .contentType(MediaType.APPLICATION_JSON)
        .content(valueAsString))
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Matheus"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Ribeiro"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("166.876.568-99"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("matheuskraot@gmail.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("5000"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("12345"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("12345"))
        .andDo(MockMvcResultHandlers.print()
        )

}
    fun `should not update a customer   with invalid id and return 400 status `() {
        val invalidId: Long = 2L
        val customerUpdateDtoDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDtoDto)

        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${invalidId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }



    private fun buildCustomerDto(
        firstName: String = "Matheus",
        lastName: String = "Ribeiro",
        cpf: String="166.876.568-99",
        email:String="matheuskraot@gmail.com",
        password:String="12345",
        zipCode:String="12345",
        street:String="12345",
        income: BigDecimal = BigDecimal.valueOf(1000.0),

    ) = CustomerDto (
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        zipCode = zipCode,
        street = street,
        password = password,
        income = income,


    )

    private fun buildCustomerUpdateDto(
        firstName: String = "Kraot",
        lastName: String = "Alves",
        zipCode:String="12345",
        street:String="12345",
        income: BigDecimal = BigDecimal.valueOf(5000.0),

        ) : CustomerUpdateDto = CustomerUpdateDto (
        firstName = firstName,
        lastName = lastName,
        zipCode = zipCode,
        street = street,
        income = income
        )



}



