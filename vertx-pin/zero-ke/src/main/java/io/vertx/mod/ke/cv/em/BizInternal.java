package io.vertx.mod.ke.cv.em;

/**
 * Lower case required here for usage
 * 1) Biz means business type
 * 2) Here are typed fixed value:
 * - TypeEntity:                The fixed value of extension table metadata
 * - TypeUser:                  The modelId of SUser
 * - TypeEmployee:              The type of EEmployee
 * - TypeCustomer:              The type of ECustomer
 * - TypeIdentity:              The type of EIdentity
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BizInternal {
    /*
     * zero.authority
     * zero.category
     * zero.close.code
     * zero.company
     * zero.customer
     * zero.employee
     * zero.identity
     * zero.integration
     * zero.tabular
     * zero.wh
     * zero.workflow.cat
     * zero.contract
     * zero.project
     */
    enum TypeEntity {
        // RBAC Module of Security
        Authority("zero.authority"),


        // Data Dict Part ( List / Tree )
        Category("zero.category"),
        Tabular("zero.tabular"),


        // Business Part of Erp System
        Company("zero.company"),
        Customer("zero.customer"),
        Contract("zero.contract"),
        Project("zero.project"),
        Employee("zero.employee"),
        Identity("zero.identity"),


        // Integration Part
        Integration("zero.integration"),
        Directory("zero.directory"),


        // Workflow Flow Type
        Wh("zero.wh"),
        CloseCode("zero.close.code"),
        WorkflowCat("zero.workflow.cat");

        // region TypeEntity
        private final String name;

        TypeEntity(final String name) {
            this.name = name;
        }

        public String value() {
            return this.name;
        }

        @Override
        public String toString() {
            return "TypeEntity{" + "name='" + this.name + '\'' + '}';
        }
        // endregion
    }

    // ---------------------------- Employee -------------------------------
    /*
     * Account / Employee information of definition inner framework
     * 1) The type of account:          TypeUser
     * 2) The type of employee:         TypeEmployee
     * 3) The status of employee:       StatusEmployee
     */
    enum TypeUser {
        employee,           // * employee         - EEmployee
        customer,           //   customer         - ECustomer
        member              //   member           - Online shop member
    }

    enum TypeEmployee {
        cw,                 //   Temp Employee but also Contract Worker
        regular,            // * The Regular Employee
        vendor,             //   The Employee came from customer of `vendor`
        third_party,        //   The third party employee ( Reserved )
    }

    enum StatusEmployee {
        running,            // * The Default Employee status ( Or NULL )
        trip,               //   In Trip
        vacation,           //   In Vacation
        training,           //   In Training
    }

    // ---------------------------- Company / Customer -------------------
    /*
     * Company / Customer information of definition inner framework
     * 1) The type of company
     * 2) The type of customer
     */
    enum TypeCompany {
        company,            // * The Company entity
        branch,             //   The Branch Company
        shop                //   The shop of some location or region
    }

    enum TypeCustomer {
        corporation,        // * The Default Customer that stored into CRM system as record
        vendor,             //   The Vendor
    }
    // ---------------------------- For Contract / Project ---------------
    /*
     * This part will be empty because all the status/types will be stored in data dict
     * database for usage and it won't be specific and contains code logical for different
     * type / status here.
     */
}
