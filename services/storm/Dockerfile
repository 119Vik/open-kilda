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

FROM kilda/base-ubuntu
# tar should be pre-populated with the tarball; see the top level Makefile (build-base:)
ADD tar /src
ADD ansible /ansible
RUN ansible-playbook -s /ansible/deploy.yml
ADD conf/storm.yaml /opt/storm/conf/storm.yaml
ADD log4j2 /opt/storm/log4j2
ADD lib /opt/local/lib
ADD lib/jackson-core-2.9.3.jar /opt/storm/lib/jackson-core-2.9.3.jar
ADD lib/jackson-databind-2.9.3.jar /opt/storm/lib/jackson-databind-2.9.3.jar
ADD lib/jackson-annotations-2.9.3.jar /opt/storm/lib/jackson-annotations-2.9.3.jar
