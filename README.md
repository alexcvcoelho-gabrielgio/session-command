# Session-command

Session-commands adds commands into event source pipeline.

This webapi simply enqueue a command into kafka to be processed by a robot.

## Running

To run it needs to connect with kafka. In near future we will create a a docker-compose or a makefile to made this easier but for now it is something like this:
```
KAFKA="localhot:9092" \
lein run
```

This is not the only way to setup env variables for more info check [environ](https://github.com/weavejester/environ#usage).

## License

Copyright © 2017 alexcvcoelho and gabrielgio
