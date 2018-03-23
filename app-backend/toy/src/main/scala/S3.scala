package com.github.aaronxsu

import com.amazonaws.auth._
import com.amazonaws.regions._
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.{AmazonS3Client => AwsAmazonS3Client}

import geotrellis.spark.io.s3.AmazonS3Client


object S3 {
  lazy val client: AwsAmazonS3Client = new AwsAmazonS3Client(new DefaultAWSCredentialsProviderChain())
  lazy val s3Client: AmazonS3Client = new AmazonS3Client(client)
}
