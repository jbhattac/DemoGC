# DemoGC

## Introduction

The aim of this project is to get introduced with the google cloud  and ways to use its api.

## Solution

The solution is based on three servlets. One of the servlet will fetch the details of the bucket `BucketDetails`.
`Bucket Details

name: demoproject-205006.appspot.com
location: EU
timeCreated: 2018-05-23T06:58:57.219Z
Full bucket details : ...
`
The other is to Add data `BucketDataIOAdd`

`Added file : json-test.txt in bucket with success .`

The last of them is to delete data `BucketDataIODelete`

`Deleted file : json-test.txt in bucket with success `

## Deployment

We can use the eclipse tooling to test and deploy the application.

However there are maven commands `mvn appengine:run`.

