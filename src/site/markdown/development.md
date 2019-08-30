# Development

## Preparations

Before you can get started developing ARSnova you need to make sure the following software is installed to build ARSnova Backend:

* OpenJDK 8 or 11 Development Kit
* Apache Maven 3.x

And additionally if you want to run ARSnova locally:

* Apache CouchDB 2.x (see [Installation Guide](installation.md#couchdb))

Next, you need to setup an ARSnova configuration file.
Create a new file [application.yml] at a location of your choosing outside of the repository:

```yaml
arsnova:
  system:
    root-url: http://localhost:8080
    api:
      expose-error-messages: true
    couchdb:
      host: localhost
      db-name: arsnova3
      username: <couchdb admin user>
      password: <couchdb admin password>
  security:
    jwt:
      secret: <random string for encryption/signing>
```

Have a look at [defaults.yml](../../main/resources/config/defaults.yml) for an overview of all available configuration settings and their defaults.


## Building

ARSnova Backend uses Maven for builds and dependency management.
You do not need to download any framework or library dependencies - Maven handles this for you.

You can create a web archive (`.war` file) by running a single command:

	$ mvn package


## Running

ARSnova builds are setup up to automatically download the Java Servlet container Jetty for development.
Run the following command to download the dependencies, and startup the backend with Jetty:

	$ mvn jetty:run -D arsnova.config-dir=</path/to/config>

After a few seconds the ARSnova API will be accessible at <http://localhost:8080/>.

You can customize the logging behavior for the development environment by appending the following parameters:

* -D arsnova.log.level=TRACE
* -D arsnova.log.level.spring=DEBUG
* -D arsnova.log.exeptions=5


## Continuous Integration

Our code repositories are located on a [GitLab server](https://git.thm.de/arsnova) for internal development.
They are automatically mirrored to [GitHub](https://github.com/thm-projects) on code changes.

Apart from mirroring GitLab CI runs various jobs to:

* check the code quality (static code analysis with SonarQube)
* build a web archive
* execute unit tests
* deploy to our staging/production servers

In addition to GitLab CI for our internal repositories, we use Travis CI which is able to run against merge requests on GitHub.
Travis CI only runs unit tests for the backend.

The current build status for the master branch:

* [![Build Status](https://travis-ci.org/thm-projects/arsnova-backend.svg?branch=master)](https://travis-ci.org/thm-projects/arsnova-backend) for ARSnova Backend
* [![Build Status](https://travis-ci.org/thm-projects/arsnova-mobile.svg?branch=master)](https://travis-ci.org/thm-projects/arsnova-mobile) for ARSnova Mobile


## Further Documentation

* [Roadmap](development/roadmap.md)
* [Domain](development/domain.md)
* [Layers](development/layers.md)
* [Caching](development/caching.md)
* [Event System](development/event-system.md)
* [API](development/api.md)
