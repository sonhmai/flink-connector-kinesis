package example
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BaseSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterAll {
  override def beforeAll(): Unit = {
    System.setProperty(
      "software.amazon.awssdk.http.service.impl",
      "software.amazon.awssdk.http.apache.ApacheSdkHttpService"
    )
  }
}
