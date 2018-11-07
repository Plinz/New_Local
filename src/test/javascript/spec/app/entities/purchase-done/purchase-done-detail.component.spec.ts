/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseDoneDetailComponent } from 'app/entities/purchase-done/purchase-done-detail.component';
import { PurchaseDone } from 'app/shared/model/purchase-done.model';

describe('Component Tests', () => {
    describe('PurchaseDone Management Detail Component', () => {
        let comp: PurchaseDoneDetailComponent;
        let fixture: ComponentFixture<PurchaseDoneDetailComponent>;
        const route = ({ data: of({ purchaseDone: new PurchaseDone(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseDoneDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PurchaseDoneDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseDoneDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.purchaseDone).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
