# Copyright 2017 Telstra Open Source
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#

import os
from py2neo import Graph

import config


def create_p2n_driver():
    graph = Graph("http://{}:{}@{}:7474/db/data/".format(
        os.environ.get('neo4juser') or config.get('neo4j', 'user'),
        os.environ.get('neo4jpass') or config.get('neo4j', 'pass'),
        os.environ.get('neo4jhost') or config.get('neo4j', 'host')))
    return graph
