package example

/** Streaming App using Table API
  */
object StreamingAppTableLocal {

  private val logger = org.log4s.getLogger

  def main(args: Array[String]): Unit = {
    DockerPostgres.start()
    DockerPostgres.execute("""
                             |CREATE TABLE IF NOT EXISTS customer (
                             | customer_code varchar(8) primary key,
                             | name text,
                             | phone_number text,
                             | created_at timestamptz not null,
                             | updated_at timestamptz
                             |);
                             |""".stripMargin)
    logger.info("Done setting up")

  }
}
