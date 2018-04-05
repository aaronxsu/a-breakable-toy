CREATE TABLE tiffs(
  id UUID PRIMARY KEY NOT NULL,
  local_file_path TEXT,
  s3_file_path TEXT
);

INSERT INTO tiffs (
  id,
  local_file_path,
  s3_file_path
) VALUES (
  '2cff116a-92d3-49de-8967-50070e1e4c2d',
  '/data/raster/test_imagery.tif',
  'https://s3.amazonaws.com/rasterfoundry-test/s3-uplaod-test/ebee-test-flight-nz233reals.tif'
);
