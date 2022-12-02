package example

import org.testcontainers.containers.{Container, GenericContainer}

import java.time.Duration

object DockerPostgres {
  private val PORT = 5432
  private val IMAGE_NAME = "postgres:13.4"
  private val POSTGRES: String = "postgres"
  private val SCHEMA: String = "testSchema"
  private val container: GenericContainer[_] = new GenericContainer(IMAGE_NAME)

  container.withStartupTimeout(Duration.ofMinutes(1))
  container.addExposedPort(PORT)
  container.withEnv("POSTGRES_USER", POSTGRES)
  container.withEnv("POSTGRES_PASSWORD", POSTGRES)
  def start(): Unit = {
    if (!container.isRunning) {
      container.start()
      createSchema()
    }
  }

  def configJDBC: ConfigJDBC = ConfigJDBC(
    jdbcUrl = s"jdbc:postgresql://${container.getHost}:${container.getMappedPort(PORT)}/postgres?currentSchema=$SCHEMA",
    username = POSTGRES,
    password = POSTGRES,
    driverName = "org.postgresql.Driver"
  )

  private def throwIfFailed(result: Container.ExecResult): Unit = {
    if (result.getExitCode != 0) throw new RuntimeException(result.toString)
  }

  def execute(sqlStatement: String): Unit = {
    val result = container.execInContainer(
      "psql",
      "-U",
      "postgres",
      "-c",
      sqlStatement
    )
    throwIfFailed(result)
  }

  private def createSchema(): Unit = {
    val result = container.execInContainer(
      "psql",
      "-U",
      "postgres",
      "-c",
      s"CREATE SCHEMA IF NOT EXISTS $SCHEMA"
    )
    throwIfFailed(result)
  }

}
