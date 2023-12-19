import toastr from 'toastr'

export const errorHandler = (error) => {
        toastr.options = {
            positionClass: 'toast-top',
            progressBar: true,
            closeButton: true,
            preventDuplicates: true,
            timeOut: 5000,
            extendedTimeOut: 1000,
            iconClass: 'custom-error-icon',
        }
        toastr.error(error)
    }