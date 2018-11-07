/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { PurchasePendingDetailComponent } from 'app/entities/purchase-pending/purchase-pending-detail.component';
import { PurchasePending } from 'app/shared/model/purchase-pending.model';

describe('Component Tests', () => {
    describe('PurchasePending Management Detail Component', () => {
        let comp: PurchasePendingDetailComponent;
        let fixture: ComponentFixture<PurchasePendingDetailComponent>;
        const route = ({ data: of({ purchasePending: new PurchasePending(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchasePendingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PurchasePendingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchasePendingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.purchasePending).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
