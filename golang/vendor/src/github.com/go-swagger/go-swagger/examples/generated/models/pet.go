package models

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the swagger generate command

import (
	"strconv"

	strfmt "github.com/go-swagger/go-swagger/strfmt"
	"github.com/go-swagger/go-swagger/swag"

	"github.com/go-swagger/go-swagger/errors"
	"github.com/go-swagger/go-swagger/httpkit/validate"
)

/*Pet pet

swagger:model Pet
*/
type Pet struct {

	/* category
	 */
	Category *Category `json:"category,omitempty"`

	/* id
	 */
	ID int64 `json:"id,omitempty"`

	/* name

	Required: true
	*/
	Name *string `json:"name"`

	/* photo urls

	Required: true
	*/
	PhotoUrls []string `json:"photoUrls"`

	/* pet status in the store
	 */
	Status string `json:"status,omitempty"`

	/* tags
	 */
	Tags []*Tag `json:"tags,omitempty"`
}

// Validate validates this pet
func (m *Pet) Validate(formats strfmt.Registry) error {
	var res []error

	if err := m.validateName(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validatePhotoUrls(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if err := m.validateTags(formats); err != nil {
		// prop
		res = append(res, err)
	}

	if len(res) > 0 {
		return errors.CompositeValidationError(res...)
	}
	return nil
}

func (m *Pet) validateName(formats strfmt.Registry) error {

	if err := validate.Required("name", "body", m.Name); err != nil {
		return err
	}

	return nil
}

func (m *Pet) validatePhotoUrls(formats strfmt.Registry) error {

	if err := validate.Required("photoUrls", "body", m.PhotoUrls); err != nil {
		return err
	}

	for i := 0; i < len(m.PhotoUrls); i++ {

		if err := validate.RequiredString("photoUrls"+"."+strconv.Itoa(i), "body", string(m.PhotoUrls[i])); err != nil {
			return err
		}

	}

	return nil
}

func (m *Pet) validateTags(formats strfmt.Registry) error {

	if swag.IsZero(m.Tags) { // not required
		return nil
	}

	for i := 0; i < len(m.Tags); i++ {

		if err := m.Tags[i].Validate(formats); err != nil {
			return err
		}

	}

	return nil
}
