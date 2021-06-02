@echo off
docker run --rm -v %CD%:/work -w /work -t debian:latest sh -c /work/hello-world.sh
