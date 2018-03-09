FROM hseeberger/scala-sbt

# Copy the contents in the current directory into the container at /opt/a-breakable-toy
COPY ./ /opt/a-breakable-toy

# Set the working directory at /opt/a-breakable-toy
WORKDIR /opt/a-breakable-toy/toy/

# In the sbt, run update and then compile on the migrations subproject
# RUN ./sbt migrations/update && \
    # ./sbt migrations/compile

EXPOSE 4567

# Run the sbt command once the container launches
CMD ["sbt"]
