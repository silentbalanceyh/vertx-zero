package io.vertx.tp.ke.cv.em;

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
     */
    enum TypeEntity {
        Authority("zero.authority"),
        Category("zero.category"),
        CloseCode("zero.close.code"),
        Company("zero.company"),
        Customer("zero.customer"),
        Employee("zero.employee"),
        Identity("zero.identity"),
        Integration("zero.integration"),
        Tabular("zero.tabular"),
        Wh("zero.wh"),
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

    enum TypeCompany {
        company,            // * The Company entity
        branch,             //   The Branch Company
        shop                //   The shop of some location or region
    }

    enum TypeCustomer {
        corporation,        // * The Default Customer that stored into CRM system as record
        vendor,             //   The Vendor
    }

    enum StatusEmployee {
        running,            // * The Default Employee status ( Or NULL )
        trip,               //   In Trip
        vacation,           //   In Vacation
        training,           //   In Training
    }
}
