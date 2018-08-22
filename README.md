[![Build Status](https://travis-ci.com/pmonks/lolcmis.svg?branch=master)](https://travis-ci.com/pmonks/lolcmis)
[![Open Issues](https://img.shields.io/github/issues/pmonks/lolcmis.svg)](https://github.com/pmonks/lolcmis/issues)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/pmonks/lolcmis.svg)](http://isitmaintained.com/project/pmonks/lolcmis "Average time to resolve an issue")
[![License](https://img.shields.io/github/license/pmonks/lolcmis.svg)](https://github.com/pmonks/lolcmis/blob/master/LICENSE)
[![Dependencies Status](https://versions.deps.co/pmonks/lolcmis/status.svg)](https://versions.deps.co/pmonks/lolcmis)

# lolcmis

An interpreter for the LOLCMIS (LOLCODE + CMIS) programming language.  See [the press release](http://blogs.alfresco.com/wp/developer/2013/04/01/alfresco-announces-hipster-compatible-enterprise-content-management-sdk/).

## Installation

Checkout the source from [GitHub](https://github.com/pmonks/lolcmis).

## Running

    $ lein run -- <filename>

## Usage:

    Switches               Default  Desc
    --------               -------  ----
    -a, --no-ast, --ast    false    Print the AST for the program, instead of evaluating it.
    -h, --no-help, --help  false    Show help

    Args       Desc
    ----       ----
    filename   The filename of the LOLCMIS program to evaluate.


## Developer Information

[GitHub project](https://github.com/pmonks/lolcmis)

[Bug Tracker](https://github.com/pmonks/lolcmis/issues)

## License

Copyright © 2013 Peter Monks (pmonks@gmail.com)

Distributed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
