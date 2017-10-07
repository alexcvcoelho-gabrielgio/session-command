# Commands

Session-commands adds commands into event source pipeline.

This webapi simply enqueue a command into kafka to be processed by a robot.

#Query

Session-query mange all read word from session domain.

It reads all data from a mongo database that has been previously organized and cached by [session-command](https://github.com/alexcvcoelho-gabrielgio/session-worker)

## Running

To run it needs to connect with kafka. In near future we will create a a docker-compose or a makefile to made this easier but for now it is something like this:
```
KAFKA="localhot:9092" \
MONGO="mongodb://localhost:27017/main" \
lein run
```

This is not the only way to setup env variables for more info check [environ](https://github.com/weavejester/environ#usage).

## License

Copyright © 2017 alexcvcoelho and gabrielgio
