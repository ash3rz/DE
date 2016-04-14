package main

import (
	"log"
	"os"

	spec "github.com/go-swagger/go-swagger/spec"
	flags "github.com/jessevdk/go-flags"

	"github.com/go-swagger/go-swagger/examples/task-tracker/restapi"
	"github.com/go-swagger/go-swagger/examples/task-tracker/restapi/operations"
)

// This file was generated by the swagger tool.
// Make sure not to overwrite this file after you generated it because all your edits would be lost!

func main() {
	swaggerSpec, err := spec.New(restapi.SwaggerJSON, "")
	if err != nil {
		log.Fatalln(err)
	}

	api := operations.NewTaskTrackerAPI(swaggerSpec)
	server := restapi.NewServer(api)
	defer server.Shutdown()

	parser := flags.NewParser(server, flags.Default)
	parser.ShortDescription = `Issue Tracker API`
	parser.LongDescription = `This application implements a very simple issue tracker.
It's implemented as an API which is described by this swagger spec document.

The go-swagger project uses this specification to test the code generation.
This document contains all possible values for a swagger definition.
This means that it exercises the framework relatively well.
`

	server.ConfigureFlags()
	for _, optsGroup := range api.CommandLineOptionsGroups {
		parser.AddGroup(optsGroup.ShortDescription, optsGroup.LongDescription, optsGroup.Options)
	}

	if _, err := parser.Parse(); err != nil {
		os.Exit(1)
	}

	server.ConfigureAPI()

	if err := server.Serve(); err != nil {
		log.Fatalln(err)
	}
}
