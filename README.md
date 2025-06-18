# clj-lolcode

[![Build Status](https://travis-ci.com/pmonks/clj-lolcode.svg?branch=master)](https://travis-ci.com/pmonks/clj-lolcode)
[![Open Issues](https://img.shields.io/github/issues/pmonks/clj-lolcode.svg)](https://github.com/pmonks/clj-lolcode/issues)
[![License](https://img.shields.io/github/license/pmonks/clj-lolcode.svg)](https://github.com/pmonks/clj-lolcode/blob/master/LICENSE)
[![Dependencies Status](https://versions.deps.co/pmonks/clj-lolcode/status.svg)](https://versions.deps.co/pmonks/clj-lolcode)
![Maintained](https://badges.ws/badge/?label=maintained&value=yes,+at+author's+discretion)

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

This work is licensed under a [Creative Commons Attribution-ShareAlike 4.0 International License](https://creativecommons.org/licenses/by-sa/4.0/).

[![CC-BY-SA-4.0-US](https://licensebuttons.net/l/by-sa/4.0/88x31.png)](https://creativecommons.org/licenses/by-sa/4.0/)
