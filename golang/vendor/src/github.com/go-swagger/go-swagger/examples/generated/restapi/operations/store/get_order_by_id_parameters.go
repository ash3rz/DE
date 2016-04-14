package store

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the swagger generate command

import (
	"net/http"

	"github.com/go-swagger/go-swagger/errors"
	"github.com/go-swagger/go-swagger/httpkit/middleware"

	strfmt "github.com/go-swagger/go-swagger/strfmt"
)

// NewGetOrderByIDParams creates a new GetOrderByIDParams object
// with the default values initialized.
func NewGetOrderByIDParams() GetOrderByIDParams {
	var ()
	return GetOrderByIDParams{}
}

// GetOrderByIDParams contains all the bound params for the get order by Id operation
// typically these are obtained from a http.Request
//
// swagger:parameters getOrderById
type GetOrderByIDParams struct {

	// HTTP Request Object
	HTTPRequest *http.Request

	/*ID of pet that needs to be fetched
	  Required: true
	  In: path
	*/
	OrderID string
}

// BindRequest both binds and validates a request, it assumes that complex things implement a Validatable(strfmt.Registry) error interface
// for simple values it will use straight method calls
func (o *GetOrderByIDParams) BindRequest(r *http.Request, route *middleware.MatchedRoute) error {
	var res []error
	o.HTTPRequest = r

	rOrderID, rhkOrderID, _ := route.Params.GetOK("orderId")
	if err := o.bindOrderID(rOrderID, rhkOrderID, route.Formats); err != nil {
		res = append(res, err)
	}

	if len(res) > 0 {
		return errors.CompositeValidationError(res...)
	}
	return nil
}

func (o *GetOrderByIDParams) bindOrderID(rawData []string, hasKey bool, formats strfmt.Registry) error {
	var raw string
	if len(rawData) > 0 {
		raw = rawData[len(rawData)-1]
	}

	o.OrderID = raw

	return nil
}
