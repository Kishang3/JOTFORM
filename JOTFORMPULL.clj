(config
(text-field
:name "subDomain"
:label "Domain name"
:placeholder "Sub-domain name" )

(password-field
:name "apiKey"
:label "Enter API key"
:placeholder "API keys goes here"
:required true))

(default-source (http/get :base-url " https://{subDomain}"
(header-params "Content-Type" "application/json"))
(paging/no-pagination)
(auth/apikey-custom-header :headerName "apikey")
(error-handler
(when :status 200 :message "Sucess")
))

(entity USERS
 (api-docs-url "https://api.jotform.com/docs/#user")
 (source (http/get :url "/user")
   (extract-path "content"))
   (fields
        username  :id
        name
        email      
        website
        time_zone
        account_type
        status
        created_at
        updated_at
        is_verified
        usage
        industry
        securityAnswer
        company
        securityQuestion
        webhooks
        doNotClone
        folderLayout
        language
        avatarUr))

 (entity SUBMISSIONS
 (api-docs-url "https://api.jotform.com/docs/#user-submissions")
 (source (http/get :url "/user/submissions")
   (extract-path "content")
   (paging/page-number
        :offset-query-param-initial-value  0
        offset-query-param-name  "offset"
        limit  1000 
        limit-query-param-name "limit"))
 (sync-plan
          (change-capture-cursor
            (query-params "orderby" "desc")
           (subset/by-time (query-params "updatedate" "$FROM")
                           (format "yyyy-MM-dd'T'HH:mm:ssZ")
                           (initial-value  "2023-01-01T00:00:00Z"))))
(field
    id     :id
    form_id
    ip
    created_at
    updated_at
    status
    new))
