(config
(password-field
:name "apiKey"
:label "Enter API key"
:placeholder "API keys goes here"
:required true))

(default-source (http/get :base-url " https://api.jotform.com"
(header-params "Content-Type" "application/json"))
(paging/no-pagination)
(auth/apikey-custom-header :headerName "apikey")
(error-handler
(when :status 404 :message "not found" :action fail)
(when :status 404 :action skip)
(when :status 429 :action rate-limit)
(when :status 401 :action refresh)
(when :status 503 :action retry))
)

(entity SUBMISSIONS
 (api-docs-url "https://api.jotform.com/docs/#user")
 (source (http/get :url "user?apiKey={apiKey}")
   (extract-path "content"))
   (fields
        usernam
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
        avatarUr
   ))












 (entity SUBMISSIONS
 (api-docs-url "https://api.jotform.com/docs/#user-submissions")
 (source (http/get :url "/user/submissions?apiKey={apiKey}")
   (extract-path "content")

  (paging/page-number
        :offset-query-param-initial-value  0,
        offset-query-param-name  "offset", 
        limit  30 ,
        limit-query-param-name "limit"
  
  )
 )

 (sync-plan
          (change-capture-cursor
            (query-params "orderby" "desc")
            (extract-path "content")
           (subset/by-time (query-params "updatedat" "$FROM")
                           (format "yyyy-MM-dd'T'HH:mm:ssZ")
                           (initial  "2023-01-01T00:00:00Z")
                        )))
(field
    id
    form_id
    ip
    created_at
    updated_at
    status
    new

    ))
