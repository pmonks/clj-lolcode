[![Build Status](https://travis-ci.com/pmonks/clj-lolcode.svg?branch=master)](https://travis-ci.com/pmonks/clj-lolcode)
[![Open Issues](https://img.shields.io/github/issues/pmonks/clj-lolcode.svg)](https://github.com/pmonks/clj-lolcode/issues)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/pmonks/clj-lolcode.svg)](http://isitmaintained.com/project/pmonks/clj-lolcode "Average time to resolve an issue")
[![License](https://img.shields.io/github/license/pmonks/clj-lolcode.svg)](https://github.com/pmonks/clj-lolcode/blob/master/LICENSE)
[![Dependencies Status](https://versions.deps.co/pmonks/clj-lolcode/status.svg)](https://versions.deps.co/pmonks/clj-lolcode)

# lolcmis

An interpreter for the [LOLCODE programming language](https://lolcode.org/) implemented in [Clojure](https://clojure.org/).

**CURRENTLY A WIP!**

## Installation

Checkout the source from [GitHub](https://github.com/pmonks/clj-lolcode).

## Running

    $ lein run -- <filename>

## Usage:

    Switches               Default  Desc
    --------               -------  ----
    -a, --no-ast, --ast    false    Print the AST for the program, instead of evaluating it.
    -h, --no-help, --help  false    Show help

    Args       Desc
    ----       ----
    filename   The filename of the LOLCODE program to evaluate.


## Developer Information

[GitHub project](https://github.com/pmonks/clj-lolcode)

[Bug Tracker](https://github.com/pmonks/clj-lolcode/issues)

## License

Copyright © 2013 Peter Monks (pmonks@gmail.com)

This work is licensed under a [Creative Commons Attribution-ShareAlike 3.0 United States License](http://creativecommons.org/licenses/by-sa/3.0/us/)

[![CC-BY-SA-3.0-US](https://i.creativecommons.org/l/by-sa/3.0/us/88x31.png)](http://creativecommons.org/licenses/by-sa/3.0/us/)